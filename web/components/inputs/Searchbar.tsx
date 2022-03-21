import { debounce } from 'lodash'
import { mix } from 'polished'
import { ChangeEvent, Dispatch, useCallback, useMemo, useState, VFC } from 'react'
import styled from 'styled-components'
import { BlockProvider, BlockProviders } from 'util/parser/types/BlockProviders'
import { exists } from '../../util'
import { forPolymorph } from '../../util/polymorphism'

interface Filter {
   mods: string[]
   text?: string
   includeExtras?: boolean
}

function matchesables(provider: BlockProvider): string[] {
   return [
      provider.name,
      ...(forPolymorph<BlockProviders, (string | undefined)[]>(provider, {
         block: p => [p.id, p.mod],
         tag: p => [p.id, p.mod],
         list: p => p.children.flatMap(matchesables),
         fallback: p => matchesables(p.children[0]),
         reference: p => (p.provider ? matchesables(p.provider) : []),
      }) ?? []),
   ].filter(exists)
}

const DEFAULT_FILTER: Filter = { mods: ['minecraft'] }

export function useFiltered<B extends BlockProvider & { mod?: string }>(unfiltered: B[]) {
   const [filter, setInstant] = useState<Filter>(DEFAULT_FILTER)
   const [lazyFilter, setLazy] = useState<Filter>(DEFAULT_FILTER)

   const filtered = useMemo(
      () =>
         unfiltered
            .filter(p => lazyFilter.includeExtras !== false || !p.extra)
            .filter(p => lazyFilter.mods.length === 0 || lazyFilter.mods.some(it => it === p.mod))
            .filter(provider => {
               const search = lazyFilter.text?.toLocaleLowerCase()
               if (!search) return true

               return matchesables(provider)
                  .map(it => it.toLowerCase())
                  .some(it => it.includes(search))
            }),
      [unfiltered, lazyFilter]
   )

   const debounced = useMemo(() => debounce(setLazy, 300), [setLazy])

   const setFilter = useCallback(
      (partial: Partial<Filter>) => {
         setInstant(it => {
            const newFilter = { ...it, ...partial }
            debounced(newFilter)
            return newFilter
         })
      },
      [setInstant, debounced]
   )

   return { filter, setFilter, filtered }
}

const Searchbar: VFC<{
   value: Filter
   onChange: Dispatch<Partial<Filter>>
}> = ({ value, onChange }) => {
   const callback = useCallback(
      (e: ChangeEvent<HTMLInputElement>) => onChange({ text: e.target.value }),
      [onChange]
   )
   return <Style placeholder='Search' onChange={callback} value={value.text ?? ''} />
}

const Style = styled.input`
   border: none;
   width: 400px;

   padding: 0.5em 1em;
   color: ${p => p.theme.text};

   outline: 1px ${p => mix(0.8, p.theme.bg, p.theme.text)} solid;
   &:focus-visible {
      outline: 2px ${p => p.theme.accent} solid;
   }
`

export default Searchbar
