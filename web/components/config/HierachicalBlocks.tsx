import { sumBy } from 'lodash'
import { useRouter } from 'next/router'
import { useCallback, useEffect, useMemo, VFC } from 'react'
import styled from 'styled-components'
import { ProviderType, TypedProvider } from '../../types/BlockProviders'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'

function isList(p: TypedProvider): p is TypedProvider<ProviderType.LIST> {
   return 'children' in p && p.children.length > 0
}

function findRecursive(path: string, blocks: TypedProvider[]): TypedProvider[] | null {
   const [search, ...rest] = path.split('/')
   if (!search) return null
   const match = blocks.find(b => b.uuid === search)
   if (match && isList(match)) {
      if (rest.length === 0) return match.children
      if (match && isList(match)) return findRecursive(rest.join('/'), match.children)
   }
   return null
}

const HierarchicalBlocks: VFC<{ blocks: TypedProvider[] }> = ({ blocks }) => {
   const router = useRouter()
   const path = router.query.path?.toString()
   const shown = useMemo(() => {
      const list = path ? findRecursive(path, blocks) : blocks
      const totalWeight = sumBy(list, it => it.weight)
      return list?.map(p => ({ ...p, weight: p.weight / totalWeight }))
   }, [blocks, path])

   useEffect(() => {
      if (shown === null) router.push({ query: {} })
   }, [router, shown])

   const navigate = useCallback(
      (provider: TypedProvider) => () => {
         if (isList(provider)) {
            const to = path ? `${path}/${provider.uuid}` : provider.uuid
            router.push({ query: { path: to } })
         }
      },
      [router, path]
   )

   return (
      <Style>
         <p>{path}</p>
         <BlockGrid>
            {shown?.map((provider, i) => (
               <ProviderPanel key={i} provider={provider} onClick={navigate(provider)} />
            ))}
         </BlockGrid>
      </Style>
   )
}

const Style = styled.div``

export default HierarchicalBlocks
