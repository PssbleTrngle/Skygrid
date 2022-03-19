import { useState, VFC } from 'react'
import styled from 'styled-components'
import DimensionConfig from 'util/parser/types/DimensionConfig'
import ActionBar from './ActionBar'
import HierarchicalBlocks from './HierachicalBlocks'
import UnwrappedBlocks from './UnwrappedBlocks'

export const enum View {
   HIERACHICAL = 'hierachical',
   UNWRAPPED = 'unwrapped',
}

const ConfigVisualizer: VFC<{ config: DimensionConfig }> = ({ config, ...props }) => {
   const [view, setView] = useState(View.HIERACHICAL)

   return (
      <Style {...props}>
         <ActionBar onView={setView} />
         {view === View.HIERACHICAL && <HierarchicalBlocks blocks={config.blocks.children} />}
         {view === View.UNWRAPPED && <UnwrappedBlocks blocks={config.blocks.children} />}
      </Style>
   )
}

const Style = styled.div``

export default ConfigVisualizer
