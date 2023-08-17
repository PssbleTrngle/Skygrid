import { FC, ReactNode } from "react";
import styled from "styled-components";
import Page from "./Page";

const Style = styled(Page)`
  display: grid;
  justify-content: center;
  align-items: center;
`;

const CenteredPage: FC<{ children: ReactNode }> = ({ children, ...props }) => (
  <Style {...props}>
    <section>{children}</section>
  </Style>
);

export default CenteredPage;
