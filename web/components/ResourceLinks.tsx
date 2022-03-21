import { groupBy } from 'lodash'
import { mix } from 'polished'
import { useMemo, VFC } from 'react'
import styled from 'styled-components'
import { Named } from 'util/parser/types'
import { InvisibleLink } from './basic/Link'

const ResourceLinks: VFC<{ keys: Named[] }> = ({ keys }) => {
   const grouped = useMemo(() => Object.entries(groupBy(keys, it => it.mod)), [keys])

   return (
      <Style>
         {grouped.map(([mod, keys]) => (
            <Group key={mod}>
               <h3>
                  <i>{mod}</i>
               </h3>
               <ul>
                  {keys.map(({ id }) => (
                     <InvisibleLink key={id} href={`./${mod}/${id}`}>
                        <ResourceLink>{id}</ResourceLink>
                     </InvisibleLink>
                  ))}
               </ul>
            </Group>
         ))}
      </Style>
   )
}

const Group = styled.div`
   padding: 2em;
   border: 1px solid ${p => mix(0.2, '#777', p.theme.bg)};

   h3 {
      margin-bottom: 0.5em;
   }

   ul {
      display: grid;
      gap: 1em;
   }
`

const ResourceLink = styled.li`
   font-size: 2em;
   padding: 0.5em 1em;
   text-align: center;

   transition: background 0.2s ease;

   background: ${p => mix(0.15, '#777', p.theme.bg)};
   &:hover {
      background: ${p => mix(0.3, '#777', p.theme.bg)};
   }
`

const Style = styled.div``

export default ResourceLinks
