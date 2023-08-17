import { createElement, DispatchWithoutAction, FC, memo } from "react";
import styled, { css } from "styled-components";
import { BlockProvider } from "schema/generated/types";
import { forPolymorph } from "../../util/polymorphism";
import Panel from "./Panel";
import BlockPanel from "./providers/BlockPanel";
import ListPanel from "./providers/ListPanel";
import ReferencePanel from "./providers/ReferencePanel";
import TagPanel from "./providers/TagPanel";

const UnknownProvider: FC<BlockProvider> = ({ __typename }) => (
  <span title={__typename}>Unknown Type</span>
);

export const panelComponent = (provider: BlockProvider): FC<any> =>
  forPolymorph(provider, {
    Block: () => BlockPanel,
    Tag: () => TagPanel,
    BlockList: () => ListPanel,
    Fallback: () => ListPanel,
    Reference: () => ReferencePanel,
  }) ?? UnknownProvider;

const ProviderPanel: FC<{
  provider: BlockProvider & { occurrences?: number };
  onClick?: DispatchWithoutAction;
  size: number;
}> = ({ provider, size, ...props }) => {
  const component = panelComponent(provider);

  return (
    <Style {...props} $reference={provider.__typename === "Reference"}>
      {createElement(component, { ...provider, size })}
      <p key="weight">{(provider.weight * 100).toFixed(4)}%</p>
      <p>
        {/*TODO DUMB PLURAL*/}
        {provider.occurrences} entr{provider.occurrences === 1 ? "y" : "ies"}
      </p>
    </Style>
  );
};

const Style = styled(Panel)<{ $reference?: boolean }>`
  ${(p) =>
    p.$reference &&
    css`
      border: ${p.theme.accent} 1px solid;
    `}
`;

export default memo(ProviderPanel);
