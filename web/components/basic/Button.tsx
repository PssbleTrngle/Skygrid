import { lighten, mix } from 'polished'
import styled, { css } from 'styled-components'
import { LinkStyle } from './Link'

const BaseStyle = css`
   cursor: pointer;
   padding: 1em 2em;
   text-align: center;

   margin: 0 auto;
   display: block;
`

export const ButtonStyle = css`
   ${BaseStyle};

   border: 1px solid ${p => lighten(0.2, p.theme.bg)};

   transition: background 0.2s ease;
   background: ${p => mix(0.15, '#777', p.theme.bg)};
   &:hover {
      background: ${p => mix(0.3, '#777', p.theme.bg)};
   }
`

const Button = styled.button`
   ${ButtonStyle}
`

export const ButtonLink = styled.button`
   ${BaseStyle};
   ${LinkStyle};
`

export default Button
