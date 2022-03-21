import { Dispatch, FC } from 'react'
import Labeled from './Labeled'

const Checkbox: FC<{
   id: string
   value: boolean
   tooltip?: string
   onChange: Dispatch<boolean>
}> = ({ id, value, onChange, tooltip, children }) => (
   <Labeled htmlFor={id} tooltip={tooltip} label={children}>
      <input id={id} type='checkbox' checked={value} onChange={e => onChange(e.target.checked)} />
   </Labeled>
)

export default Checkbox
