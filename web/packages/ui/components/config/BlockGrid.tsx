import styled from 'styled-components'

const BlockGrid = styled.div<{ size: number }>`
   display: grid;
   gap: 1em;
   padding: 10px;
   grid-template-columns: repeat(auto-fit, ${p => p.size}px);
   grid-auto-rows: 1fr;
`

export default BlockGrid
