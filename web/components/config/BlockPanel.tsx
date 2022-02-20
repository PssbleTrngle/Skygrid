import { VFC } from 'react'
import styled from 'styled-components'
import { GeneratedBlock } from '../../types/BlockProviders'
import BlockIcon from './BlockIcon'
import Panel from './Panel'

const BlockPanel: VFC<{ block: GeneratedBlock; size: number }> = ({ block, size, ...props }) => (
   <Style>
      <p>
         {block.mod ?? 'minecraft'}:{block.id}
      </p>
      <p>{(block.weight * 100).toFixed(2)}%</p>
      <BlockIcon {...block} size={size} />
   </Style>
)

const Style = styled(Panel)`
   img {
      image-rendering: pixelated;
   }
`

export default BlockPanel
