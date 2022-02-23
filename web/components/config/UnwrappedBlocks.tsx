import { groupBy, orderBy, sumBy } from 'lodash'
import { useMemo, VFC } from 'react'
import {
   Block,
   BlockProvider,
   BlockProviders,
   GeneratedBlock,
   ProviderType
} from '../../types/BlockProviders'
import WeightedEntry from '../../types/WeightedEntry'
import { forPolymorph } from '../../util/polymorphism'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'
import Searchbar, { useFiltered } from './Searchbar'

const UnwrappedBlocks: VFC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
   const unwrapped = useMemo(() => unwrap(blocks), [blocks])
   const sorted = useMemo(() => orderBy(unwrapped, b => b.weight, 'desc'), [unwrapped])
   const size = 100

   const { filter, setFilter, filtered } = useFiltered(sorted)

   console.log(filter)

   return (
      <>
         <Searchbar value={filter} onChange={setFilter} />
         <BlockGrid size={size}>
            {filtered.map(block => (
               <ProviderPanel key={block.uuid} size={size} provider={block}>
                  <p>
                     {/*TODO DUMB PLURAL*/}
                     {block.occurenced} entr{block.occurenced === 1 ? 'y' : 'ies'}
                  </p>
               </ProviderPanel>
            ))}
         </BlockGrid>
      </>
   )
}

function withWeight<T extends Required<WeightedEntry>>(
   blocks: T[],
   func: (w: number) => number = w => w
): T[] {
   return blocks.map(b => ({ ...b, weight: func(b.weight) }))
}

function unwrapProvider(provider: BlockProvider): Block[] {
   return (
      forPolymorph<BlockProviders, Block[]>(provider, {
         block: p => withWeight([p]),
         tag: p => withWeight(p.matches, w => w / p.matches.length),
         list: p => withWeight(unwrap(p.children), w => w * p.weight),
         fallback: p => withWeight(unwrap(p.children).slice(0), () => p.weight),
         reference: p => withWeight(unwrapProvider(p.provider), () => p.weight),
      }) ?? []
   )
}

function unwrap(providers: BlockProvider[]): GeneratedBlock[] {
   const unwrapped = providers.flatMap(p => unwrapProvider(p))
   const total = sumBy(unwrapped, w => w.weight)
   const normalized = unwrapped
      .map(p => ({ ...p, weight: p.weight / total }))
      .flatMap<Omit<GeneratedBlock, 'occurenced' | 'type'>>(({ extras, ...provider }) => {
         const unwrappedExtras = extras
            ? extras.flatMap(it => withWeight(unwrap(it.children), w => w * it.probability))
            : []
         return [
            { ...provider, extra: false },
            ...withWeight(unwrappedExtras, w => w * provider.weight).map(e => ({
               ...e,
               extra: true,
            })),
         ]
      })

   return Object.values(groupBy(normalized, n => `${n.mod}:${n.id}`)).map(occurences => ({
      ...occurences[0],
      occurenced: occurences.length,
      weight: sumBy(occurences, it => it.weight),
      type: ProviderType.BLOCK,
   }))
}

export default UnwrappedBlocks
