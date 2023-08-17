import { MaterialShadow } from "../styles/mixins";
import { lighten } from "polished";
import styled from "styled-components";

const Panels = styled.div`
  display: grid;
  justify-content: center;
  margin-top: 4rem;

  gap: 100px;
  grid-auto-rows: 1fr;
  grid-template-columns: repeat(auto-fit, 500px);

  & > div {
    padding: 2em;
    transition: background 0.3s ease,
      box-shadow 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
    display: grid;
    align-items: center;

    border-radius: 1em;
    ${MaterialShadow};

    &:hover {
      background: ${(p) => lighten(0.005, p.theme.bg)};
    }
  }
`;

export default Panels;
