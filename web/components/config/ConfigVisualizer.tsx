import { VFC } from 'react'
import styled from 'styled-components'
import DimensionConfig from '../../types/DimensionConfig'
import UnwrappedBlocks from './UnwrappedBlocks'

const ConfigVisualizer: VFC<{ config: DimensionConfig }> = ({ config, ...props }) => {
   return (
      <Style {...props}>
         <UnwrappedBlocks blocks={config.blocks.children} />
      </Style>
   )
}

const Style = styled.div``

export default ConfigVisualizer
