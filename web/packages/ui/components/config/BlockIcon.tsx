import { FC, useEffect, useMemo, useState } from "react";
import { BlockProvider, Named } from "schema/generated/types";
import styled from "styled-components";

const FALLBACK = `/unknown_block.png`;

const BlockIcon: FC<BlockProvider & Named & { size: number }> = ({
  size,
  id,
  icon,
}) => {
  const defaultSrc = useMemo(() => icon ?? FALLBACK, [icon]);
  const [src, setSrc] = useState<string>(defaultSrc);

  useEffect(() => setSrc(defaultSrc), [defaultSrc]);

  return (
    <Style alt={id} src={src} size={size} onError={() => setSrc(FALLBACK)} />
  );
};

const Style = styled.img<{ size: number }>`
  object-fit: contain;
  height: ${(p) => `${p.size}px`};
  width: ${(p) => `${p.size}px`};
`;

export default BlockIcon;
