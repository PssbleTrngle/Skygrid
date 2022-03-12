import type { AppProps } from 'next/app'
import { createGlobalStyle, ThemeProvider } from 'styled-components'
import { LocalFileProvider } from '../hooks/useLocalFiles'
import '../styles/reset.css'
import dark from '../theme/dark'

function MyApp({ Component, pageProps }: AppProps) {
   return (
      <ThemeProvider theme={dark}>
         <LocalFileProvider>
            <Global />
            <Component {...pageProps} />
         </LocalFileProvider>
      </ThemeProvider>
   )
}

const Global = createGlobalStyle`
   body, html {
      scroll-behavior: smooth;
      font-family: sans-serif;
      background: ${p => p.theme.bg};
      color: ${p => p.theme.text};
   }
`

export default MyApp
