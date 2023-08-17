import { FC, ReactNode } from 'react'
import styled from 'styled-components'

const Labeled: FC<{
   htmlFor: string
   label?: ReactNode
   tooltip?: string
   prefix?: boolean
   children: ReactNode
}> = ({ htmlFor, label, children, prefix, tooltip }) => (
   <Style>
      {prefix || children}
      {label && (
         <label title={tooltip} htmlFor={htmlFor}>
            {label}
         </label>
      )}
      {prefix && children}
   </Style>
)

const Style = styled.div`
   display: grid;
   align-items: center;
   gap: 1em;

   width: max-content;
   grid-auto-flow: column;
`

export default Labeled
