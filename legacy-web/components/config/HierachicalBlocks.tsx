import { omit, sumBy } from 'lodash'
import { useRouter } from 'next/router'
import { useCallback, useEffect, useMemo, VFC } from 'react'
import styled from 'styled-components'
import { BlockList, BlockProvider, BlockProviders } from 'util/parser/types/BlockProviders'
import { forPolymorph, MorphMap } from 'util/polymorphism'
import { exists } from '../../util'
import Breadcrumbs from '../Breadcrumbs'
import BlockGrid from './BlockGrid'
import ProviderPanel from './ProviderPanel'

function recurseList(it: BlockList) {
   return (rest: string[]) => {
      if (rest.length === 0) return it.children
      return findRecursive(rest, it.children)
   }
}

const recurseStep: MorphMap<BlockProviders, (rest: string[]) => BlockProvider[] | null> = {
   list: recurseList,
   fallback: recurseList,
   reference: it => rest => {
      console.log(it)
      const children = [it.provider].filter(exists)
      if (rest.length === 0) return children
      return findRecursive(rest, children)
   },
}

function findRecursive(path: string[], blocks: BlockProvider[]): BlockProvider[] | null {
   const [search, ...rest] = path
   if (!search) return null
   const match = blocks.find(b => b.uuid === search || b.name === search)
   if (match) return forPolymorph(match, recurseStep)?.(rest) ?? null
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
      if (shown === null) router.push({ query: omit(router.query, 'to') })
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
         if (Object.keys(recurseStep).includes(provider.type)) {
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
