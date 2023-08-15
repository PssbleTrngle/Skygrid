import { FC, useEffect, useMemo, useReducer } from "react";
import { Tag } from "schema/generated/types";
import BlockIcon from "../BlockIcon";

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
      <p>{tag.mod ?? "minecraft"}</p>
      <p>#{tag.id}</p>
      <p>{tag.matches.length} matches</p>
    </>
  );
};

export default TagPanel;
