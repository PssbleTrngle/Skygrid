import { FC, useCallback, useEffect, useMemo, useState } from "react";
import { BlockProvider, Named } from "schema/generated/types";
import Image from "../basic/Image";

const FALLBACK = `/unknown_block.png`;

const BlockIcon: FC<BlockProvider & Named & { size: number }> = ({
  id,
  icon,
  size,
}) => {
  const defaultSrc = useMemo(() => icon ?? FALLBACK, [icon]);
  const [src, setSrc] = useState<string>(defaultSrc);
  const onError = useCallback(() => setSrc(FALLBACK), [setSrc]);

  useEffect(() => setSrc(defaultSrc), [defaultSrc]);

  return (
    <Image
      src={src}
      size={size}
      objectFit="contain"
      onError={onError}
      alt={id}
    />
  );
};

export default BlockIcon;
