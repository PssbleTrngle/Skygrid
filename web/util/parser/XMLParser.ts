import { uniq } from 'lodash'
import { nanoid } from 'nanoid'
import getConfig from 'next/config'
import {
   Block,
   BlockProvider,
   BlockProviders,
   ProviderType,
   Reference,
   Tag,
} from 'util/parser/types/BlockProviders'
import DimensionConfig from 'util/parser/types/DimensionConfig'
import { Extra, ExtrasType } from 'util/parser/types/Extras'
import Preset from 'util/parser/types/Preset'
import { Parser } from 'xml2js'
import { exists } from '..'
import { forPolymorph, Polymorpher, toArray } from '../polymorphism'
import DataResolver from './DataResolver'
import { Named } from './types'

const { publicRuntimeConfig } = getConfig()

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

export enum ResourceType {
   CONFIG = 'dimensions',
   PRESET = 'presets',
}

interface ResourceTypes {
   [ResourceType.CONFIG]: DimensionConfig
   [ResourceType.PRESET]: Preset
}

export type Resource<T extends ResourceType> = ResourceTypes[T]

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

export type TagValue =
   | string
   | {
        id: string
        required?: boolean
     }

export interface TagDefinition {
   replace?: boolean
   values: TagValue[]
}

function merge<T>(a: T, b: T): T {
   if (!a) return b
   if (!b) return a
   const keys = uniq([...Object.keys(a), ...Object.keys(b)]) as (keyof T)[]
   const entries = keys.map(key => [key, [a[key], b[key]].filter(exists)])
   return Object.fromEntries(entries) as T
}

export default class XMLParser {
   private polymorpher = new Polymorpher()

   constructor(private resolver: DataResolver) {
      this.polymorpher.register<BlockProvider>(
         'children',
         ProviderType,
         async (provider, index, parent) => {
            const weight = provider.weight ?? 1
            const uuid =
               provider.type === ProviderType.BLOCK ? nanoid(8) : `${provider.type}-${index + 1}`

            const except = merge(provider.except, parent?.except)

            const extra = await forPolymorph<BlockProviders, Promise<{}> | {}>(provider, {
               tag: async p => ({
                  matches: await this.getBlocksFor({ ...p, except }),
               }),
               block: b => this.extendBlock(b),
               reference: async p => ({ provider: await this.getPreset(p) }),
            })

            return { ...provider, ...extra, weight, uuid }
         }
      )

      this.polymorpher.register<Extra>('extras', ExtrasType, async e => ({
         ...e,
         probability: e.probability ?? 1,
      }))
   }

   async getResources(type: ResourceType) {
      const namespaces = await this.resolver.list('directory', 'data')
      const resouces = await Promise.all(
         namespaces.map(async mod => {
            const dir = ['data', mod, 'skygrid', type]
            if (!(await this.resolver.exists('directory', ...dir))) return []
            const files = await this.resolver.list('file', ...dir)
            return Promise.all(
               files
                  .filter(f => f.endsWith('xml'))
                  .map(async file => ({
                     mod,
                     id: file.substring(0, file.length - 4),
                     lastModified: (await this.resolver.lastModified?.(...dir, file)) ?? null,
                  }))
            )
         })
      )
      return resouces.flat()
   }

   async getConfig(key: Named) {
      const path = this.getDataPath(key, `skygrid/${ResourceType.CONFIG}`, 'xml')
      return this.parseFile<DimensionConfig>(...path)
   }

   async getPreset(reference: Reference): Promise<BlockProvider | null> {
      const path = this.getDataPath(reference, `skygrid/${ResourceType.PRESET}`, 'xml')
      const parsed = await this.parseFile<Preset>(...path)
      return parsed?.children?.[0] ?? null
   }

   async getIcon(block: Named) {
      const mod = block.mod ?? 'minecraft'
      const icon = `${publicRuntimeConfig.basePath}/blocks/${mod}/${block.id}.png`
      if (await this.resolver.exists('file', 'public', icon)) return icon
      return null
   }

   async extendBlock(block: Block) {
      return {
         mod: block.mod ?? 'minecraft',
         name: await this.resolver.getName(block),
         icon: await this.getIcon(block),
      }
   }

   private getDataPath({ id, mod }: Named, type: string, extension: string) {
      return ['data', mod ?? 'minecraft', type, `${id}.${extension}`]
   }

   async tagDefinition(tag: Pick<Tag, 'id' | 'mod' | 'except'>) {
      const path = this.getDataPath(tag, 'tags', 'json')
      const content = await this.resolver.getContent(...path)
      if (!content) return null
      try {
         return JSON.parse(content) as TagDefinition
      } catch {
         return null
      }
   }

   async getBlocksFor(tag: Pick<Tag, 'id' | 'mod' | 'except'>): Promise<Block[]> {
      const definition = await this.tagDefinition(tag)
      if (!definition) return []

      const filters: Array<(block: Block) => boolean> = []

      if (tag.except) {
         toArray(tag.except.mod).forEach(({ id }) => filters.push(b => b.mod === id))
         toArray(tag.except.name).forEach(({ pattern }) =>
            filters.push(b => `${b.mod}${b.id}`.includes(pattern))
         )

         const tagFilters = await Promise.all(
            toArray(tag.except.tag).map(t => this.getBlocksFor(t))
         )

         tagFilters.forEach(matches =>
            filters.push(a => matches.some(b => a.id === b.id && a.mod === b.mod))
         )
      }

      const resolved = await Promise.all(
         definition.values.map(async value => {
            const tagID = typeof value === 'string' ? value : value.id
            const [mod, id] = tagID.split(':')
            if (tagID.startsWith('#')) return this.getBlocksFor({ mod: mod.substring(1), id })
            else return [{ id, mod, weight: 1, uuid: nanoid(8) }]
         })
      )

      const extended = await Promise.all(
         resolved.flat().map<Promise<Block>>(async b => ({ ...b, ...(await this.extendBlock(b)) }))
      )

      return extended.filter(b => !filters.some(it => it(b))).filter(exists) ?? []
   }

   private async parseFile<T extends object>(...path: string[]) {
      const content = await this.resolver.getContent(...path)
      if (!content) return null
      return this.parse<T>(content)
   }

   private async parse<T extends object>(input: string) {
      const parser = new Parser({
         explicitRoot: false,
         mergeAttrs: true,
         explicitArray: false,
         attrValueProcessors: [
            modify(NUMERICS, Number.parseFloat),
            modify(['id'], it => (it.startsWith('#') ? it.substring(1) : it)),
         ],
      })

      const parsed: T = await parser.parseStringPromise(input)

      return this.polymorpher.applyPolymorphs(parsed)
   }
}
