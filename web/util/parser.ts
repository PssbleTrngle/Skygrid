import { readFileSync } from 'fs'
import { nanoid } from 'nanoid'
import { Parser } from 'xml2js'
import { BlockProviders, ProviderType, TypedProvider } from '../types/BlockProviders'
import DimensionConfig from '../types/DimensionConfig'
import { blocksForTag, nameOf } from './data'
import { applyPolymorphs, forPolymorph, registerPolymorph } from './polymorphism'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

registerPolymorph<TypedProvider>('children', ProviderType, provider => {
   const weight = provider.weight ?? 1
   const uuid = nanoid(8)
   const extra = forPolymorph<BlockProviders, {}>(provider, {
      tag: p => ({ matches: blocksForTag(p) }),
      block: p => ({ name: nameOf(p) }),
   })
   return { ...provider, ...extra, weight, uuid }
})

export default async function parseConfig(input: string) {
   const parser = new Parser({
      explicitRoot: false,
      mergeAttrs: true,
      explicitArray: false,
      attrValueProcessors: [
         modify(NUMERICS, Number.parseFloat),
         modify(['id'], it => (it.startsWith('#') ? it.substring(1) : it)),
      ],
   })

   const parsed: DimensionConfig = await parser.parseStringPromise(input)

   return applyPolymorphs(parsed)
}

export function parseFile(file: string) {
   return parseConfig(readFileSync(file).toString())
}
