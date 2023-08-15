import { LinkProps, LinkStyle } from "./Link";
import { lighten, mix } from "polished";
import styled, { css } from "styled-components";

const BaseStyle = css`
  cursor: pointer;
  padding: 1em 2em;
  text-align: center;

  margin: 0 auto;
  display: block;

  &:disabled {
    color: ${(p) => mix(0.8, "#AAA", p.theme.text)};
    cursor: not-allowed;
  }
`;

export const ButtonStyle = css`
  ${BaseStyle};

  border: 1px solid ${(p) => lighten(0.2, p.theme.bg)};

  transition: background 0.2s ease;
  background: ${(p) => mix(0.15, "#777", p.theme.bg)};

  &:hover {
    background: ${(p) => mix(0.3, "#777", p.theme.bg)};
  }
`;

const Button = styled.button`
  ${ButtonStyle}
`;

export const ButtonLink = styled.button<LinkProps>`
  ${BaseStyle};
  ${LinkStyle};
`;

export default Button;
