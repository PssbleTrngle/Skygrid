import { orderBy } from "lodash-es";
import { FC, useMemo } from "react";
import styled from "styled-components";
import { BlockList, BlockProvider } from "schema/generated/types";
import BlockIcon from "../BlockIcon";
import unwrapBlocks from "../../../util/unwrapBlocks";
import { PanelName } from "./NamedLines";

const Previewed = styled.div<{ $size: number; $columns: number }>`
  display: grid;
  grid-template-columns: repeat(${(p) => p.$columns}, 1fr);
  align-content: flex-start;
  min-height: ${(p) => p.$size}px;
`;

const MAX_SHOWN = 16;

const ListPanel: FC<
  BlockProvider &
    BlockList & {
      size: number;
    }
> = ({ name, children, size, __typename }) => {
  const blocks = useMemo(
    () =>
      orderBy(
        unwrapBlocks(children).filter((p) => !p.extra),
        (p) => !p.icon
      )?.slice(0, MAX_SHOWN),
    [children]
  );

  const blocksPerColumn = useMemo(() => {
    return Math.ceil(Math.sqrt(blocks.length));
  }, [blocks]);

  return (
    <>
      <Previewed $size={size} $columns={blocksPerColumn}>
        {blocks.map((block, i) => (
          <BlockIcon {...block} key={i} size={size / blocksPerColumn} />
        ))}
      </Previewed>
      <PanelName>{name ?? <i>{__typename}</i>}</PanelName>
      <p>({children.length} entries)</p>
    </>
  );
};

export default ListPanel;
