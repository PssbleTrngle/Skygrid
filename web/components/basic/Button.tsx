import { mix } from 'polished'
import styled from 'styled-components'

const Button = styled.button`
   cursor: pointer;
   padding: 1em 2em;

   margin: 0 auto;
   display: block;

   transition: background 0.2s ease;
   background: ${p => mix(0.15, '#777', p.theme.bg)};
   &:hover {
      background: ${p => mix(0.3, '#777', p.theme.bg)};
   }
`

export default Button
