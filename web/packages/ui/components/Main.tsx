import { lighten } from "polished";
import styled from "styled-components";

const Main = styled.main`
  padding: 2rem 4rem 8rem;
  background: ${(p) => p.theme.bg};
  border-top: 0.25rem solid ${(p) => lighten(0.04, p.theme.bg)};
  box-shadow: -10px 0 40px 0 #000;
`;

export default Main;
