import { lighten } from 'polished'
import { Dispatch, Fragment, VFC } from 'react'
import styled, { css } from 'styled-components'

type crumb = string

const Breadcrumbs: VFC<{
   crumbs: crumb[]
   onClick?: Dispatch<crumb[]>
   root?: string
}> = ({ crumbs, onClick, root, ...props }) => (
   <Style {...props}>
      {root && (
         <>
            <Crumb clickable={!!onClick && crumbs.length > 0} onClick={() => onClick?.([])}>
               {root}
            </Crumb>
            <span>{'>'}</span>
         </>
      )}
      {crumbs.map((crumb, i) => (
         <Fragment key={`${crumb}-${i}`}>
            {i > 0 && <span>/</span>}
            <Crumb
               clickable={!!onClick && i < crumbs.length - 1}
               onClick={() => onClick?.(crumbs.slice(0, i + 1))}>
               {crumb}
            </Crumb>
         </Fragment>
      ))}
   </Style>
)

const Crumb = styled.span<{ clickable: boolean }>`
   font-style: italic;

   ${p =>
      p.clickable &&
      css`
         cursor: pointer;
         &:hover {
            text-decoration: underline;
         }
      `}
`

const Style = styled.p`
   cursor: default;
   display: grid;
   grid-auto-flow: column;
   gap: 0.5em;
   justify-content: start;
   padding: 1em;
   background: ${p => lighten(0.1, p.theme.bg)};
   margin-bottom: 2em;
`

export default Breadcrumbs
