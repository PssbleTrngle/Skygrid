import Dropdown from 'components/inputs/Dropdown'
import { groupBy, isString, orderBy, sumBy, uniq } from 'lodash'
import { useMemo, useState, VFC } from 'react'
import styled from 'styled-components'
import {
   BlockProvider,
   BlockProviders,
   GeneratedBlock,
   GeneratedTag,
   ProviderType,
} from 'util/parser/types/BlockProviders'
import WeightedEntry from 'util/parser/types/WeightedEntry'
import { forPolymorph } from '../../util/polymorphism'
import Checkbox from '../inputs/Checkbox'
import Searchbar, { useFiltered } from '../inputs/Searchbar'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'

const wrap = (value: string) => ({ value, label: value.replace(/[_-]/g, ' ') })

const UnwrappedBlocks: VFC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
   const [unwrapTags, setUnwrapTags] = useState(false)

   const unwrapped = useMemo(() => unwrap(blocks, !unwrapTags), [blocks, unwrapTags])

   const sorted = useMemo(() => orderBy(unwrapped, b => b.weight, 'desc'), [unwrapped])
   const size = 100

   const mods = useMemo(
      () =>
         uniq(unwrapped.map(it => it.mod?.toLowerCase()))
            .filter(isString)
            .map(wrap),
      [unwrapped]
   )

   const { filter, setFilter, filtered } = useFiltered(sorted)

   return (
      <>
         <FilterBar>
            <Searchbar value={filter} onChange={setFilter} />
            <Checkbox
               id='includeExtras'
               tooltip='Extras are blocks that are generated adjacent to the base'
               value={filter.includeExtras ?? true}
               onChange={v => setFilter({ includeExtras: v })}>
               Include Extras?
            </Checkbox>
            <Checkbox id='unwrapTsgs' value={unwrapTags} onChange={setUnwrapTags}>
               Unwrap Tags?
            </Checkbox>
            <Dropdown
               id='mods'
               label='Mods:'
               isClearable
               isMulti
               options={mods}
               value={filter.mods?.map(wrap)}
               onChange={v => setFilter({ mods: v.map(it => it.value) })}
            />
         </FilterBar>
         <Results>{filtered.length} blocks</Results>
         <BlockGrid size={size}>
            {filtered.map(block => (
               <ProviderPanel key={block.uuid} size={size} provider={block} />
            ))}
         </BlockGrid>
      </>
   )
}

const Results = styled.p`
   font-style: italic;
   text-align: center;
   margin-bottom: 1em;
`

const FilterBar = styled.section`
   display: grid;
   grid-auto-flow: column;
   height: 5em;
   margin-bottom: 2em;
   align-items: center;
   justify-content: start;
   gap: 3em;
`

function withWeight<T extends Required<WeightedEntry>>(
   blocks: T[],
   func: (w: number) => number = w => w
): T[] {
   return blocks.map(b => ({ ...b, weight: func(b.weight) }))
}

function unwrapProvider(provider: BlockProvider, wrappedTags?: boolean): BlockProvider[] {
   return (
      forPolymorph<BlockProviders, BlockProvider<any>[]>(provider, {
         block: p => [p as BlockProvider],
         tag: p => (wrappedTags ? [p] : withWeight(p.matches, w => w / p.matches.length)),
         list: p => withWeight(unwrap(p.children, wrappedTags), w => w * p.weight),
         fallback: p => withWeight(unwrap(p.children, wrappedTags).slice(0, 1), () => p.weight),
         reference: p =>
            p.provider ? withWeight(unwrapProvider(p.provider, wrappedTags), () => p.weight) : [],
      }) ?? []
   )
}

export function unwrap<TWrappedTags extends boolean = true>(
   providers: BlockProvider[],
   wrappedTags?: TWrappedTags
): Array<TWrappedTags extends true ? GeneratedTag | GeneratedBlock : GeneratedBlock> {
   const unwrapped = providers.flatMap(p => unwrapProvider(p, wrappedTags)) as GeneratedBlock[]
   const total = sumBy(unwrapped, w => w.weight)
   const normalized = unwrapped
      .map(p => ({ ...p, weight: p.weight / total }))
      .flatMap(({ extras, ...provider }) => {
         const unwrappedExtras = extras
            ? extras.flatMap(it =>
                 withWeight(unwrap(it.children, wrappedTags), w => w * it.probability)
              )
            : []

         return [
            { ...provider, extra: provider.extra ?? false },
            ...withWeight(unwrappedExtras, w => w * provider.weight).map(e => ({
               ...e,
               extra: true,
            })),
         ]
      })

   return Object.values(groupBy(normalized, n => `${n.mod}:${n.id}`)).map((occurences, i) => ({
      ...occurences[0],
      occurenced: occurences.length,
      weight: sumBy(occurences, it => it.weight),
      uuid: `unwrapped-${i}`,
      type: occurences[0].type ?? ProviderType.BLOCK,
   }))
}

export default UnwrappedBlocks
