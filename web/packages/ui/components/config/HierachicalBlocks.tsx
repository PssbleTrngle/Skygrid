import { FC, useCallback } from "react";
import styled from "styled-components";
import { BlockProvider } from "schema/generated/types";
import { createId } from "../../util";
import BlockGrid from "./BlockGrid";
import ProviderPanel from "./ProviderPanel";
import { canBeRecursed, useNavigateToPath } from "../../util/findRecursive";
import { PanelName } from "./providers/NamedLines";

const HierarchicalBlocks: FC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
  const navigate = useNavigateToPath();

  const click = useCallback(
    (provider: BlockProvider, index: number) => () => {
      if (canBeRecursed(provider)) {
        const id = provider.name ?? createId(provider, index);
        navigate((path) => [...path, id]);
      }
    },
    [navigate]
  );

  return (
    <Style>
      <BlockGrid size={200}>
        {blocks?.map((provider, i) => (
          <ProviderPanel
            key={createId(provider, i)}
            size={200}
            provider={provider}
            onClick={click(provider, i)}
          />
        ))}
      </BlockGrid>
    </Style>
  );
};

const Style = styled.div`
  ${PanelName} {
    font-size: 1.2em;
  }
`;

export default HierarchicalBlocks;
