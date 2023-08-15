import { Book } from '@styled-icons/boxicons-regular/Book'
import { Bug } from '@styled-icons/boxicons-regular/Bug'
import { CodeAlt } from '@styled-icons/boxicons-regular/CodeAlt'
import { Book as SolidBook } from '@styled-icons/boxicons-solid/Book'
import { Bug as SolidBug } from '@styled-icons/boxicons-solid/Bug'
import { Download } from '@styled-icons/boxicons-solid/Download'
import { VFC } from 'react'
import styled from 'styled-components'
import { IconLink } from './basic/Link'

const LinkBar: VFC = () => (
   <Style>
      <IconLink icon={Download} href='https://www.curseforge.com/minecraft/mc-mods/skygrid'>
         Download the mod
      </IconLink>

      <IconLink icon={CodeAlt} href='https://github.com/PssbleTrngle/Skygrid'>
         Look at the source
      </IconLink>

      <IconLink
         icon={Bug}
         hoverIcon={SolidBug}
         href='https://github.com/PssbleTrngle/Skygrid/issues'>
         Report an issue
      </IconLink>

      <IconLink
         icon={Book}
         hoverIcon={SolidBook}
         href='https://github.com/PssbleTrngle/Skygrid/wiki'>
         Browse the wiki
      </IconLink>
   </Style>
)

const Style = styled.nav`
   font-size: 3rem;
   display: grid;
   grid-template-columns: repeat(2, 1fr);
   margin: 3rem auto;
   width: max-content;
   gap: 0.5em 2em;
   padding: 1em 0;

   svg + span {
      margin-left: 0.5em;
   }
`

export default LinkBar
