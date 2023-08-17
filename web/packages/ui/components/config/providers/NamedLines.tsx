import { FC } from "react";
import { BlockProvider, Named } from "schema/generated/types";
import styled from "styled-components";
import { mix } from "polished";

const NamedLines: FC<
  Named &
    Pick<BlockProvider, "name"> & {
      prefix?: string;
    }
> = ({ prefix = "", id, mod, name }) => {
  const resolvedMod = mod ?? "minecraft";
  return (
    <>
      <PanelName title={`${prefix}${resolvedMod}:${id}`}>
        {name ?? (
          <Id>
            {prefix}
            {id}
          </Id>
        )}
      </PanelName>
      <ModName>{resolvedMod}</ModName>
    </>
  );
};

export const PanelName = styled.p`
  text-wrap: wrap;
  max-height: 3em;
`;

const Id = styled.i`
  line-break: anywhere;
`;

const ModName = styled.p`
  color: ${(p) => mix(0.4, p.theme.bg, p.theme.text)};
  margin: 0.5em 0;
  font-size: 0.8em;
`;

export default NamedLines;
