import { FC } from 'react'
import { GeneratedBlock } from '../../types/BlockProviders'
import BlockIcon from './BlockIcon'

const BlockPanel: FC<GeneratedBlock & { size: number }> = ({ children, ...block }) => (
   <>
      <BlockIcon {...block} />
      <span>{block.name ?? <i>{block.id}</i>}</span>
      <code>{block.mod ?? 'minecraft'}</code>
      {children}
   </>
)

export default BlockPanel
