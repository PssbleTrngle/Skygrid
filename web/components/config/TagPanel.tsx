import { FC } from 'react'
import { Tag } from '../../types/BlockProviders'
import BlockIcon from './BlockIcon'

const TagPanel: FC<Tag & { size: number }> = ({ size, children, ...tag }) => {
   const [block] = tag.matches
   return (
      <>
         <BlockIcon {...block} size={size} />
         <p>{tag.mod ?? 'minecraft'}</p>
         <p>{tag.id}</p>
         <p>{tag.matches.length} matches</p>
         {children}
      </>
   )
}

export default TagPanel
