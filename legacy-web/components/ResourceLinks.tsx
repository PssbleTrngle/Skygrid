import { groupBy } from 'lodash'
import { useMemo, VFC } from 'react'
import styled from 'styled-components'
import { Named } from 'util/parser/types'
import { ButtonStyle } from './basic/Button'
import Link from './basic/Link'

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
                     <Link underline='none' key={id} href={`./${mod}/${id}`}>
                        <ResourceLink>{id}</ResourceLink>
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
   //border: 1px solid ${p => p.theme.text};

   h3 {
      margin-bottom: 1.5em;
   }

   ul {
      margin-left: 2em;
      display: grid;
      gap: 1em;

      li {
         position: relative;

         &::before,
         &::after {
            content: '';
            position: absolute;
         }

         &::before {
            height: calc(100% + 1em);
            left: -0.75em;
            top: calc(-50% - 1em);
            border-left: 1px solid ${p => p.theme.text};
         }

         &::after {
            width: 0.5em;
            left: -0.75em;
            top: 25%;
            height: 25%;
            border: 1px solid transparent;
            border-bottom-color: ${p => p.theme.text};
            //transform: rotate(45deg);
         }
      }

      & > :first-child {
         li::before {
            top: -0.5em;
         }
      }
   }
`

const ResourceLink = styled.li`
   ${ButtonStyle};
   font-size: 2em;
   padding: 0.5em 1em;
`

const Style = styled.div``

export default ResourceLinks