import { VFC } from 'react'
import { GeneratedBlock } from 'util/parser/types/BlockProviders'
import BlockIcon from '../BlockIcon'

const BlockPanel: VFC<GeneratedBlock & { size: number }> = ({ ...block }) => (
   <>
      <BlockIcon {...block} />
      <p>{block.name ?? <i>{block.id}</i>}</p>
      <code>{block.mod ?? 'minecraft'}</code>
   </>
)

export default BlockPanel
