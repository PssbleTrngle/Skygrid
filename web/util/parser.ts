import { existsSync, readFileSync } from 'fs'
import { nanoid } from 'nanoid'
import { resolve } from 'path'
import { Parser } from 'xml2js'
import { Block, BlockProvider, BlockProviders, ProviderType } from '../@types/BlockProviders'
import { Extra, ExtrasType } from '../@types/Extras'
import { nameOf } from './data'
import { getStaticReference } from './data/configs'
import { blocksForTag } from './data/tags'
import { forPolymorph, Polymorpher } from './polymorphism'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

const polymorpher = new Polymorpher()

function extendBlock(p: Block) {
   const icon = `blocks/${p.mod ?? 'minecraft'}/${p.id}.png`
   const hasIcon = existsSync(resolve('public', icon))
   return {
      name: nameOf(p),
      icon: hasIcon ? icon : null,
   }
}

polymorpher.register<BlockProvider>('children', ProviderType, async provider => {
   const weight = provider.weight ?? 1
   const uuid = nanoid(8)
   const extra = await forPolymorph<BlockProviders, Promise<{}> | {}>(provider, {
      tag: p => ({ matches: blocksForTag(p).map(b => ({ ...b, ...extendBlock(b) })) }),
      block: extendBlock,
      reference: async p => ({ provider: await getStaticReference(p) }),
   })
   return { ...provider, ...extra, weight, uuid }
})

polymorpher.register<Extra>('extras', ExtrasType, async e => ({
   ...e,
   probability: e.probability ?? 1,
}))

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

   return polymorpher.applyPolymorphs(parsed)
}

export function parseFile<T extends object>(file: string) {
   return parseXML<T>(readFileSync(file).toString())
}
