import Image from 'next/image'
import { VFC } from 'react'
import styled from 'styled-components'

const Background: VFC = () => (
   <Style>
      <Image
         src='/screenshot.png'
         layout='fill'
         objectFit='cover'
         alt='screenshot of the generation'
      />
   </Style>
)

const Style = styled.section`
   top: 0;
   left: 0;
   position: fixed;
   width: 100vw;
   height: 100vh;
   filter: blur(0.5rem) brightness(0.75);
`

export default Background
