import React from 'react'
import ReactDOM from 'react-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { BrowserRouter } from 'react-router-dom'
import { createGlobalStyle, ThemeProvider } from 'styled-components'
import App from './App'
import { SessionProvider } from './hooks/useSession'
import reportWebVitals from './reportWebVitals'
import dark from './themes/dark'

const Global = createGlobalStyle`
  body, html {
    background: ${p => p.theme.bg};
    color: ${p => p.theme.text};
    font-family: sans-serif;
  }
`

const client = new QueryClient({
   defaultOptions: {
      queries: {
         refetchInterval: 1000,
         retry: false,
      }
   }
})

ReactDOM.render(
   <React.StrictMode>
      <BrowserRouter>
         <ThemeProvider theme={dark}>
            <Global />
            <QueryClientProvider client={client}>
               <SessionProvider>
                  <App />
               </SessionProvider>
            </QueryClientProvider>
         </ThemeProvider>
      </BrowserRouter>
   </React.StrictMode>,
   document.getElementById('root')
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals()
