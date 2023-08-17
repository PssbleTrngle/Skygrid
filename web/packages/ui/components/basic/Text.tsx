import styled from "styled-components";
import { UnderlineHover } from "../../styles/mixins";

export const Title = styled.h1`
  font-size: 3rem;
  text-align: center;
  margin-bottom: 2rem;
`;

export const Subtitle = styled.h2`
  text-align: center;
  margin-bottom: 2rem;
`;
export const Centered = styled.p`
  text-align: center;
`;

export const Underlined = styled.span`
  ${UnderlineHover}
`;
