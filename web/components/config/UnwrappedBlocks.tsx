import { groupBy, orderBy, sumBy } from 'lodash'
import { useMemo, VFC } from 'react'
import styled from 'styled-components'
import {
   Block,
   BlockProvider,
   BlockProviders,
   GeneratedBlock,
   ProviderType,
} from 'util/parser/types/BlockProviders'
import WeightedEntry from 'util/parser/types/WeightedEntry'
import { forPolymorph } from '../../util/polymorphism'
import Checkbox from '../inputs/Checkbox'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'
import Searchbar, { useFiltered } from './Searchbar'

const UnwrappedBlocks: VFC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
   const unwrapped = useMemo(() => unwrap(blocks), [blocks])
   const sorted = useMemo(() => orderBy(unwrapped, b => b.weight, 'desc'), [unwrapped])
   const size = 100

   const { filter, setFilter, filtered } = useFiltered(sorted)

   return (
      <>
         <FilterBar>
            <Searchbar value={filter} onChange={setFilter} />
            <Checkbox
               id='includeExtras'
               value={filter.includeExtras ?? true}
               onChange={v => setFilter({ includeExtras: v })}>
               Include Extras?
            </Checkbox>
            <p>{filtered.length} results</p>
         </FilterBar>
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

const FilterBar = styled.section`
   display: grid;
   grid-auto-flow: column;
   margin-bottom: 2em;
   align-items: center;
   gap: 1em;
`

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
         fallback: p => withWeight(unwrap(p.children).slice(0, 1), () => p.weight),
         reference: p => (p.provider ? withWeight(unwrapProvider(p.provider), () => p.weight) : []),
      }) ?? []
   )
}

export function unwrap(providers: BlockProvider[]): GeneratedBlock[] {
   const unwrapped = providers.flatMap(p => unwrapProvider(p))
   const total = sumBy(unwrapped, w => w.weight)
   const normalized = unwrapped
      .map(p => ({ ...p, weight: p.weight / total }))
      .flatMap<Omit<GeneratedBlock, 'occurenced' | 'type'>>(({ extras, ...provider }) => {
         const unwrappedExtras = extras
            ? extras.flatMap(it => withWeight(unwrap(it.children), w => w * it.probability))
            : []

         return [
            { ...provider, extra: provider.extra ?? false },
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
