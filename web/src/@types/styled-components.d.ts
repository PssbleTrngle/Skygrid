import 'styled-components';

declare module 'styled-components' {
   interface DefaultTheme {
      bg: string
      primary: string
      secondary: string
      text: string
      error: string
      warning: string
      ok: string
      link: {
         default: string
         visited: string
      }
   }
}
