import { FC, useCallback, useEffect, useMemo, useState } from "react";
import { BlockProvider, Named } from "schema/generated/types";
import { useElementFactory } from "../../context/elements";

const FALLBACK = `/unknown_block.png`;

const BlockIcon: FC<BlockProvider & Named & { size: number }> = ({
  size,
  id,
  icon,
}) => {
  const defaultSrc = useMemo(() => icon ?? FALLBACK, [icon]);
  const [src, setSrc] = useState<string>(defaultSrc);
  const onError = useCallback(() => setSrc(FALLBACK), [setSrc]);

  useEffect(() => setSrc(defaultSrc), [defaultSrc]);

  const { createImg } = useElementFactory();

  return createImg({
    src,
    size,
    objectFit: "contain",
    onError,
    alt: id,
  });
};

export default BlockIcon;
