import { VFC } from 'react'
import styled from 'styled-components'
import DimensionConfig from '../../types/DimensionConfig'
import HierarchicalBlocks from './HierachicalBlocks'

const ConfigVisualizer: VFC<{ config: DimensionConfig }> = ({ config, ...props }) => {
   return (
      <Style {...props}>
         <HierarchicalBlocks blocks={config.blocks.children} />
      </Style>
   )
}

const Style = styled.div``

export default ConfigVisualizer
