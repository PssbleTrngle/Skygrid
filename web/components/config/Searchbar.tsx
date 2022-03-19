import { debounce } from 'lodash'
import { ChangeEvent, Dispatch, useCallback, useMemo, useState, VFC } from 'react'
import styled from 'styled-components'
import { BlockProvider, BlockProviders } from 'util/parser/types/BlockProviders'
import { exists } from '../../util'
import { forPolymorph } from '../../util/polymorphism'

interface Filter {
   mod?: string[]
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

export function useFiltered<B extends BlockProvider>(unfiltered: B[]) {
   const [filter, setInstant] = useState<Filter>({})
   const [lazyFilter, setLazy] = useState<Filter>({})

   const filtered = useMemo(
      () =>
         unfiltered
            .filter(p => lazyFilter.includeExtras !== false || !p.extra)
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
            const filter = { ...it, ...partial }
            debounced(filter)
            return filter
         })
      },
      [setInstant, debounced]
   )

   return { filter, setFilter, filtered }
}

const Searchbar: VFC<{
   value: Filter
   onChange: Dispatch<Filter>
}> = ({ value, onChange }) => {
   const callback = useCallback(
      (e: ChangeEvent<HTMLInputElement>) => onChange({ text: e.target.value }),
      [onChange]
   )
   return <Style placeholder='Search' onChange={callback} value={value.text ?? ''} />
}

const Style = styled.input`
   padding: 1em 2em;
   background: #0001;
`

export default Searchbar
