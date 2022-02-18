import { lighten, transparentize } from 'polished'
import styled from 'styled-components'

const Button = styled.button`
   outline: none;
   border: none;

   background: ${p => p.theme.primary};
   padding: 1em;
   width: fit-content;

   display: grid;
   align-items: center;
   justify-content: center;

   border-radius: 99999px;
   cursor: pointer;

   transition: all 0.1s ease;

   &:hover {
      background: ${p => lighten(0.1, p.theme.primary)};
   }

   &:hover,
   &:focus-visible {
      outline: 2px solid ${p => lighten(0.1, p.theme.primary)};
   }

   &:hover {
      box-shadow: 0 0 0 5px ${p => transparentize(0.5, p.theme.primary)};
   }
`

export default Button
