import { useEffect, useReducer, VFC } from 'react'
import { Tag } from '../../../@types/BlockProviders'
import BlockIcon from '../BlockIcon'

const TagPanel: VFC<Tag & { size: number }> = ({ size, ...tag }) => {
   const [viewed, tick] = useReducer((i: number) => (i + 1) % tag.matches.length, 0)
   const block = tag.matches[viewed]

   useEffect(() => {
      const interval = setInterval(tick, 1500)
      return () => clearInterval(interval)
   }, [tick])

   return (
      <>
         <BlockIcon {...block} size={size} />
         <p>{tag.mod ?? 'minecraft'}</p>
         <p>{tag.id}</p>
         <p>{tag.matches.length} matches</p>
      </>
   )
}

export default TagPanel
