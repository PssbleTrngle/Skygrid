import styled from 'styled-components'

const Panel = styled.div<{ size?: number }>`
   cursor: pointer;
   text-align: center;

   padding: 0.1em;
   gap: 0.1em;

   display: grid;
   justify-content: center;
   align-items: center;

   font-size: ${p => (p.size ?? 150) / 150}em;

   &:hover {
      background: #0001;
   }
`

export default Panel
