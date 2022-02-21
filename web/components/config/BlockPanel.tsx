import { FC } from 'react'
import { GeneratedBlock } from '../../types/BlockProviders'
import BlockIcon from './BlockIcon'

const BlockPanel: FC<GeneratedBlock & { size: number }> = ({ children, ...block }) => (
   <>
      <p>{block.name ?? block.id}</p>
      <p>{block.mod ?? 'minecraft'}</p>
      {children}
      <BlockIcon {...block} />
   </>
)

export default BlockPanel
