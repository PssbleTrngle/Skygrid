import { FC, useEffect, useMemo, useReducer } from "react";
import { Tag } from "schema/generated/types";
import BlockIcon from "../BlockIcon";
import NamedLines from "./NamedLines";

const TagPanel: FC<Tag & { size: number }> = ({ size, ...tag }) => {
  const displayed = useMemo(() => tag.matches.filter((it) => !!it.icon), [tag]);
  const [viewed, tick] = useReducer(
    (i: number) => (i + 1) % displayed.length,
    0
  );
  const block = displayed[viewed];

  useEffect(() => {
    const interval = setInterval(tick, 1500);
    return () => clearInterval(interval);
  }, [tick]);

  return (
    <>
      <BlockIcon {...block} size={size} />
      <NamedLines {...tag} prefix="#" />
      <p>{tag.matches.length} matches</p>
    </>
  );
};

export default TagPanel;
