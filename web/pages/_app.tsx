import Footer from 'components/Footer'
import { FileSystemProvider } from 'hooks/useFileSystem'
import type { AppProps } from 'next/app'
import { createGlobalStyle, ThemeProvider } from 'styled-components'
import '../styles/reset.css'
import dark from '../styles/theme/dark'

function MyApp({ Component, pageProps }: AppProps) {
   return (
      <ThemeProvider theme={dark}>
         <Global />
         <FileSystemProvider>
            <Component {...pageProps} />
            <Footer />
         </FileSystemProvider>
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

   ul, ol {
      list-style: none;
   }
`

export default MyApp
