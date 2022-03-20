import { groupBy } from 'lodash'
import Link from 'next/link'
import { useRouter } from 'next/router'
import { mix } from 'polished'
import { useMemo, VFC } from 'react'
import styled from 'styled-components'
import { Named } from 'util/parser/types'

const ResourceLinks: VFC<{ keys: Named[] }> = ({ keys }) => {
   const { asPath } = useRouter()

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
                     <Link key={id} href={`${asPath}/${mod}/${id}`} passHref>
                        <InvisibleLink>
                           <ResourceLink>{id}</ResourceLink>
                        </InvisibleLink>
                     </Link>
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

const InvisibleLink = styled.a`
   text-decoration: none;
   color: ${p => p.theme.text};
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
