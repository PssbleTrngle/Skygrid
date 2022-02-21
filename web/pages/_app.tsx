import type { AppProps } from 'next/app'
import { createGlobalStyle } from 'styled-components'
import '../styles/reset.css'

function MyApp({ Component, pageProps }: AppProps) {
   return (
      <>
         <Global />
         <Component {...pageProps} />
      </>
   )
}

const Global = createGlobalStyle`
   body, html {
      scroll-behavior: smooth;
      font-family: sans-serif;
   }
`

export default MyApp
