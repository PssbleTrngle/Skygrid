import { createGlobalStyle, css } from "styled-components";
import { lighten } from "polished";

const ScrollbarRadius = css`
  border-top-left-radius: 0.5rem;
  border-bottom-left-radius: 0.5rem;
`;

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

  ::-webkit-scrollbar {
    width: unset;
  }

  ::-webkit-scrollbar-button {
    background: ${(p) => p.theme.bg};
  }

  ::-webkit-scrollbar-track {
    ${ScrollbarRadius};
    background: ${(p) => lighten(0.1, p.theme.bg)};
  }

  ::-webkit-scrollbar-thumb {
    ${ScrollbarRadius};
    cursor: grab;
    transition: background 0.2s ease;
    background: ${(p) => lighten(0.3, p.theme.bg)};
  }

  ::-webkit-scrollbar-thumb:hover {
    background: ${(p) => lighten(0.5, p.theme.bg)};
  }
`;

export default Global;
