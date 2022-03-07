import { Dispatch, FC } from 'react'
import styled from 'styled-components'

const Checkbox: FC<{
   id: string
   value: boolean
   onChange: Dispatch<boolean>
}> = ({ id, value, onChange, children }) => (
   <Style>
      <input
         id='includeExtras'
         type='checkbox'
         checked={value}
         onChange={e => onChange(e.target.checked)}
      />
      <Label htmlFor='includeExtras'>{children}</Label>
   </Style>
)

const Label = styled.label`
   margin-left: 1em;
`

const Style = styled.div``

export default Checkbox
