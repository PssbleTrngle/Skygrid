import Head from 'next/head'
import { FC } from 'react'
import styled from 'styled-components'

const Page: FC = ({ children, ...props }) => (
   <Style {...props}>
      <Head>
         <title>Skygrid</title>
         <meta name='description' content='Analyze skygrid configurations' />
         <link rel='icon' href='/favicon.ico' />
      </Head>
      {children}
   </Style>
)

const Style = styled.main``

export default Page
