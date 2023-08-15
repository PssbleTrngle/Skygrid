import { createGlobalStyle } from "styled-components";

const Global = createGlobalStyle`
   body, html {
      scroll-behavior: smooth;
      font-family: sans-serif;
      background: ${(p) => p.theme.bg};
      color: ${(p) => p.theme.text};
   }

   ul, ol {
      list-style: none;
   }
`;

export default Global;
