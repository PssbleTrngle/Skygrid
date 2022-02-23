import { readFileSync } from 'fs'
import { nanoid } from 'nanoid'
import { Parser } from 'xml2js'
import { BlockProviders, ProviderType, TypedProvider } from '../types/BlockProviders'
import { nameOf } from './data'
import { getStaticReference } from './data/configs'
import { blocksForTag } from './data/tags'
import { applyPolymorphs, forPolymorph, registerPolymorph } from './polymorphism'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

registerPolymorph<TypedProvider>('children', ProviderType, async provider => {
   const weight = provider.weight ?? 1
   const uuid = nanoid(8)
   const extra = await forPolymorph<BlockProviders, Promise<{}> | {}>(provider, {
      tag: p => ({ matches: blocksForTag(p) }),
      block: p => ({ name: nameOf(p) }),
      reference: async p => ({ provider: await getStaticReference(p) }),
   })
   return { ...provider, ...extra, weight, uuid }
})

export default async function parseXML<T extends object>(input: string) {
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

   return applyPolymorphs(parsed)
}

export function parseFile<T extends object>(file: string) {
   return parseXML<T>(readFileSync(file).toString())
}
