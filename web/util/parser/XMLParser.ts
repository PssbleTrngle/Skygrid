import { nanoid } from 'nanoid'
import { Parser } from 'xml2js'
import { exists } from '..'
import { Named } from '../../@types'
import {
   Block,
   BlockProvider,
   BlockProviders,
   ProviderType,
   Reference,
   Tag,
} from '../../@types/BlockProviders'
import DimensionConfig from '../../@types/DimensionConfig'
import { Extra, ExtrasType } from '../../@types/Extras'
import Preset from '../../@types/Preset'
import { forPolymorph, Polymorpher, toArray } from '../polymorphism'
import DataResolver from './DataResolver'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

export interface TagDefinition {
   replace?: boolean
   values: string[]
}

export default class XMLParser {
   private polymorpher = new Polymorpher()

   constructor(private resolver: DataResolver) {
      this.polymorpher.register<BlockProvider>('children', ProviderType, async provider => {
         const weight = provider.weight ?? 1
         const uuid = nanoid(8)
         const extra = await forPolymorph<BlockProviders, Promise<{}> | {}>(provider, {
            tag: async p => ({
               matches: await this.getBlocksFor(p),
            }),
            block: b => this.extendBlock(b),
            reference: async p => ({ provider: await this.getPreset(p) }),
         })
         return { ...provider, ...extra, weight, uuid }
      })

      this.polymorpher.register<Extra>('extras', ExtrasType, async e => ({
         ...e,
         probability: e.probability ?? 1,
      }))
   }

   async getResources(type: string): Promise<Named[]> {
      const namespaces = await this.resolver.list('data')
      const resouces = await Promise.all(
         namespaces.map(async mod => {
            const files = await this.resolver.list('data', mod, 'skygrid', type)
            return files
               .filter(f => f.endsWith('xml'))
               .map<Named>(file => ({ mod, id: file.substring(0, file.length - 4) }))
         })
      )
      return resouces.flat()
   }

   async getConfig(key: Named) {
      const path = this.getDataPath(key, 'skygrid/dimensions', 'xml')
      return this.parseFile<DimensionConfig>(...path)
   }

   async getPreset(reference: Reference) {
      const path = this.getDataPath(reference, 'skygrid/presets', 'xml')
      const parsed = await this.parseFile<Preset>(...path)
      return parsed?.children?.[0]
   }

   async extendBlock(block: Block) {
      const icon = `blocks/${block.mod ?? 'minecraft'}/${block.id}.png`
      const hasIcon = await this.resolver.exists('public', icon)
      return {
         name: await this.resolver.getName(block),
         icon: hasIcon ? icon : null,
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
            const [mod, id] = value.split(':')
            if (value.startsWith('#')) return this.getBlocksFor({ mod: mod.substring(1), id })
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
