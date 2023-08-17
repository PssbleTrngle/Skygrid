import { darken } from 'polished'
import styled from 'styled-components'

const Panel = styled.div<{ size?: number }>`
   cursor: pointer;
   text-align: center;

   padding: 0.1em;
   gap: 0.1em;

   display: grid;
   justify-content: center;
   align-items: center;

   font-size: ${p => (p.size ?? 150) / 150}em;

   overflow: hidden;
   white-space: nowrap;
   text-overflow: ellipsis;

   &:hover {
      background: ${p => darken(0.01, p.theme.bg)};
   }
`

export default Panel
