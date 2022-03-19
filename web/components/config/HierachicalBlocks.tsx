import { omit, sumBy } from 'lodash'
import { useRouter } from 'next/router'
import { useCallback, useEffect, useMemo, VFC } from 'react'
import styled from 'styled-components'
import { BlockProvider, ProviderType } from 'util/parser/types/BlockProviders'
import Breadcrumbs from '../Breadcrumbs'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'

function isList(p: BlockProvider): p is BlockProvider<ProviderType.LIST> {
   return 'children' in p && p.children.length > 0
}

function findRecursive(path: string[], blocks: BlockProvider[]): BlockProvider[] | null {
   const [search, ...rest] = path
   if (!search) return null
   const match = blocks.find(b => b.uuid === search || b.name === search)
   if (match && isList(match)) {
      if (rest.length === 0) return match.children
      if (isList(match)) return findRecursive(rest, match.children)
   }
   return null
}

const HierarchicalBlocks: VFC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
   const router = useRouter()
   const path = useMemo(
      () =>
         router.query.path
            ?.toString()
            .split('/')
            .map(s => s.trim())
            .filter(s => !!s) ?? [],
      [router.query]
   )

   const shown = useMemo(() => {
      const list = path.length ? findRecursive(path, blocks) : blocks
      const totalWeight = sumBy(list, it => it.weight)
      return list?.map(p => ({ ...p, weight: p.weight / totalWeight }))
   }, [blocks, path])

   useEffect(() => {
      if (shown === null) router.push({ query: {} })
   }, [router, shown])

   const navigate = useCallback(
      (to: string[]) => {
         if (to.length) router.push({ query: { ...router.query, path: to.join('/') } })
         else router.push({ query: omit(router.query, 'path') })
      },
      [router]
   )

   const click = useCallback(
      (provider: BlockProvider) => () => {
         if (isList(provider)) {
            const id = provider.name ?? provider.uuid
            navigate([...path, id])
         }
      },
      [navigate, path]
   )

   return (
      <Style>
         {path && (
            <Breadcrumbs root={router.query.config as string} crumbs={path} onClick={navigate} />
         )}
         <BlockGrid size={200}>
            {shown?.map((provider, i) => (
               <ProviderPanel key={i} size={200} provider={provider} onClick={click(provider)} />
            ))}
         </BlockGrid>
      </Style>
   )
}

const Style = styled.div``

export default HierarchicalBlocks
