import styled from 'styled-components'

const BlockGrid = styled.div`
   display: grid;
   gap: 5px;
   padding: 10px;
   grid-template-columns: repeat(auto-fit, 200px);
   grid-auto-rows: 250px;
`

export default BlockGrid
