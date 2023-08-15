import { darken, lighten, transparentize } from 'polished'
import { css } from 'styled-components'

export const Neumorphism = css`
   border-radius: 30px;
   box-shadow: 8px 8px 24px ${p => darken(0.05, p.theme.bg)},
      -8px -8px 24px ${p => lighten(0.05, p.theme.bg)};
`

export const MaterialShadow = css`
   box-shadow: 0 10px 20px ${p => darken(0.06, p.theme.bg)},
      0 6px 6px ${p => darken(0.12, p.theme.bg)};

   &:hover {
      //background: ${p => transparentize(0.5, p.theme.bg)};
      box-shadow: 0 19px 38px ${p => darken(0.08, p.theme.bg)},
         0 15px 12px ${p => darken(0.09, p.theme.bg)};
   }
`

export const UnderlineHover = css`
   position: relative;

   &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: 0;
      height: 0.1em;

      background: ${p => p.theme.text};

      width: 0;
      transition: width 0.2s ease;
   }

   &:hover::after {
      width: 100%;
   }
`
