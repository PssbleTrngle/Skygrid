import HomeLink from 'components/HomeIcon'
import { Dispatch, FC } from 'react'
import styled, { css } from 'styled-components'
import { View } from './ConfigVisualizer'

const ActionBar: FC<{
   view: View
   onView: Dispatch<View>
}> = ({ onView, view, children, ...props }) => {
   return (
      <Style {...props}>
         <HomeLink />
         {children}
         <Button active={view === View.HIERACHICAL} onClick={() => onView(View.HIERACHICAL)}>
            Hierachical
         </Button>
         <Button active={view === View.UNWRAPPED} onClick={() => onView(View.UNWRAPPED)}>
            Unwrapped
         </Button>
      </Style>
   )
}

const Button = styled.button<{ active?: boolean }>`
   margin-right: 1em;
   padding: 1em;

   ${p =>
      p.active &&
      css`
         outline: 2px solid ${p.theme.accent};
      `}
`

const Style = styled.nav`
   display: flex;
   align-items: center;
   margin-bottom: 1em;
   gap: 2em;
`

export default ActionBar
