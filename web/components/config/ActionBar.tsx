import { Dispatch, VFC } from 'react'
import styled from 'styled-components'
import { View } from './ConfigVisualizer'

const ActionBar: VFC<{
   onView: Dispatch<View>
}> = ({ onView, ...props }) => {
   return (
      <Style {...props} area-description='views'>
         <Button onClick={() => onView(View.HIERACHICAL)}>Hierachical</Button>
         <Button onClick={() => onView(View.UNWRAPPED)}>Unwrapped</Button>
      </Style>
   )
}

const Button = styled.button`
   margin-right: 1em;
   padding: 1em;
`

const Style = styled.nav``

export default ActionBar
