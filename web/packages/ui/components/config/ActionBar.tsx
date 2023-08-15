import { Dispatch, FC, ReactNode } from "react";
import styled, { css } from "styled-components";
import { View } from "./ConfigVisualizer";

const ActionBar: FC<{
  view: View;
  onView: Dispatch<View>;
  children: ReactNode;
}> = ({ onView, view, children, ...props }) => {
  return (
    <Style {...props}>
      {children}
      <Button
        active={view === View.HIERARCHICAL}
        onClick={() => onView(View.HIERARCHICAL)}
      >
        Hierachical
      </Button>
      <Button
        active={view === View.UNWRAPPED}
        onClick={() => onView(View.UNWRAPPED)}
      >
        Unwrapped
      </Button>
    </Style>
  );
};

const Button = styled.button<{ active?: boolean }>`
  margin-right: 1em;
  padding: 1em;

  ${(p) =>
    p.active &&
    css`
      outline: 2px solid ${p.theme.accent};
    `}
`;

const Style = styled.nav`
  display: flex;
  align-items: center;
  margin-bottom: 1em;
  gap: 2em;
`;

export default ActionBar;
