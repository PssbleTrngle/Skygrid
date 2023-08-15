import { Block } from "schema/generated/types";
import { FC } from "react";
import BlockIcon from "../BlockIcon";

const BlockPanel: FC<Block & { size: number }> = ({ ...block }) => (
  <>
    <BlockIcon {...block} />
    <p>{block.name ?? <i>{block.id}</i>}</p>
    <code>{block.mod ?? "minecraft"}</code>
  </>
);

export default BlockPanel;
