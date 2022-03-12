import { nanoid } from 'nanoid'
import { Parser } from 'xml2js'
import { BlockProvider, BlockProviders, ProviderType } from '../../@types/BlockProviders'
import { Extra, ExtrasType } from '../../@types/Extras'
import { nameOf } from '../data'
import { forPolymorph, Polymorpher } from '../polymorphism'
import DataResolver from './DataResolver'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

function extendBlock(p: Block) {
   const icon = `blocks/${p.mod ?? 'minecraft'}/${p.id}.png`
   const hasIcon = existsSync(resolve('public', icon))
   return {
      name: nameOf(p),
      icon: hasIcon ? icon : null,
   }
}

export default class XMLParser {
   private polymorpher = new Polymorpher()

   constructor(private resolver: DataResolver) {
      this.polymorpher.register<BlockProvider>('children', ProviderType, async provider => {
         const weight = provider.weight ?? 1
         const uuid = nanoid(8)
         const extra = await forPolymorph<BlockProviders, Promise<{}> | {}>(provider, {
            tag: p => ({
               matches: resolver.getBlocksFor(p).map(b => ({ ...b, ...extendBlock(b) })),
            }),
            block: extendBlock,
            reference: async p => ({ provider: await resolver.getPreset(p) }),
         })
         return { ...provider, ...extra, weight, uuid }
      })

      this.polymorpher.register<Extra>('extras', ExtrasType, async e => ({
         ...e,
         probability: e.probability ?? 1,
      }))
   }

   async parse<T extends object>(input: string) {
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
