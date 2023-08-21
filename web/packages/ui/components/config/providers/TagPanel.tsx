import { FC, useEffect, useMemo, useReducer } from "react";
import { Tag } from "schema/generated/types";
import BlockIcon from "../BlockIcon";
import NamedLines from "./NamedLines";
import { useAnimationPreference } from "../../../util/localPreferences";

const TagPanel: FC<Tag & { size: number }> = ({ size, ...tag }) => {
  const [shouldCycle] = useAnimationPreference();

  const displayed = useMemo(() => tag.matches.filter((it) => !!it.icon), [tag]);
  const [viewed, tick] = useReducer(
    (i: number) => (i + 1) % displayed.length,
    0
  );
  const block = displayed[viewed];

  console.log("render", shouldCycle);

  useEffect(() => {
    console.log("effect", shouldCycle);
    if (!shouldCycle) return;
    const interval = setInterval(tick, 1500);
    return () => clearInterval(interval);
  }, [tick, shouldCycle]);

  return (
    <>
      <BlockIcon {...block} size={size} />
      <NamedLines {...tag} prefix="#" />
      <p>{tag.matches.length} matches</p>
    </>
  );
};

export default TagPanel;
