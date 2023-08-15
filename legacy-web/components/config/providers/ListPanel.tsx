import { orderBy } from 'lodash'
import { useMemo, VFC } from 'react'
import styled from 'styled-components'
import { BlockList, BlockProvider } from 'util/parser/types/BlockProviders'
import BlockIcon from '../BlockIcon'
import { unwrap } from '../UnwrappedBlocks'

const Previewed = styled.div`
   display: grid;
   grid-template-columns: repeat(5, 1fr);
`

const ListPanel: VFC<
   BlockProvider &
      BlockList & {
         size: number
      }
> = ({ name, children, size, type }) => {
   const blocks = useMemo(
      () =>
         orderBy(
            unwrap(children).filter(p => !p.extra),
            p => !p.icon
         )?.slice(0, 20),
      [children]
   )

   return (
      <>
         <Previewed>
            {blocks.map((block, i) => (
               <BlockIcon {...block} key={i} size={size / 5} />
            ))}
         </Previewed>
         <p>{name ?? <i>{type}</i>}</p>
         <p>({children.length} entries)</p>
      </>
   )
}

export default ListPanel
