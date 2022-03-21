import { useRouter } from 'next/router'
import { useCallback, useEffect, VFC } from 'react'
import styled from 'styled-components'
import DimensionConfig from 'util/parser/types/DimensionConfig'
import ActionBar from './ActionBar'
import HierarchicalBlocks from './HierachicalBlocks'
import UnwrappedBlocks from './UnwrappedBlocks'

export enum View {
   HIERACHICAL = 'hierachical',
   UNWRAPPED = 'unwrapped',
}

const ConfigVisualizer: VFC<{ config: DimensionConfig }> = ({ config, ...props }) => {
   const { query, replace } = useRouter()

   const setView = useCallback(
      (v: View) => replace({ query: { ...query, view: v } }),
      [query, replace]
   )

   useEffect(() => {
      if (query.view && !Object.values(View).includes(query.view as View)) {
         setView(View.HIERACHICAL)
      }
   }, [query, setView])

   return (
      <Style {...props}>
         <ActionBar onView={setView} />
         {query.view === View.HIERACHICAL && <HierarchicalBlocks blocks={config.blocks.children} />}
         {query.view === View.UNWRAPPED && <UnwrappedBlocks blocks={config.blocks.children} />}
      </Style>
   )
}

const Style = styled.div``

export default ConfigVisualizer
