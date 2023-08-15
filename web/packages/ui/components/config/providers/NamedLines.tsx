import { FC } from "react";
import { BlockProvider, Named } from "schema/generated/types";
import styled from "styled-components";

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
          <i>
            {prefix}
            {id}
          </i>
        )}
      </PanelName>
      <p>{resolvedMod}</p>
    </>
  );
};

export const PanelName = styled.p`
  font-size: 1.2em;
  margin-bottom: 0.5em;
`;

export default NamedLines;
