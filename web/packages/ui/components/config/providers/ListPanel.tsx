import { orderBy } from "lodash-es";
import { FC, useMemo } from "react";
import styled from "styled-components";
import { BlockList, BlockProvider } from "schema/generated/types";
import BlockIcon from "../BlockIcon";
import { unwrap } from "../UnwrappedBlocks";

const Previewed = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
`;

const ListPanel: FC<
  BlockProvider &
    BlockList & {
      size: number;
    }
> = ({ name, children, size, __typename }) => {
  const blocks = useMemo(
    () =>
      orderBy(
        unwrap(children).filter((p) => !p.extra),
        (p) => !p.icon
      )?.slice(0, 20),
    [children]
  );

  return (
    <>
      <Previewed>
        {blocks.map((block, i) => (
          <BlockIcon {...block} key={i} size={size / 5} />
        ))}
      </Previewed>
      <p>{name ?? <i>{__typename}</i>}</p>
      <p>({children.length} entries)</p>
    </>
  );
};

export default ListPanel;
