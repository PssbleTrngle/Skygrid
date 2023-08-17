import { Block } from "schema/generated/types";
import { FC } from "react";
import BlockIcon from "../BlockIcon";
import NamedLines from "./NamedLines";

const BlockPanel: FC<Block & { size: number }> = ({ ...block }) => (
  <>
    <BlockIcon {...block} />
    <NamedLines {...block} />
  </>
);

export default BlockPanel;
