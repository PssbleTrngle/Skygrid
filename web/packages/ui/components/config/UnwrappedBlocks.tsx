import Dropdown from "../inputs/Dropdown";
import { isString, orderBy, uniq } from "lodash-es";
import { FC, useMemo, useState } from "react";
import styled from "styled-components";
import { BlockProvider } from "schema/generated/types";
import Checkbox from "../inputs/Checkbox";
import Searchbar, { useFiltered } from "../inputs/Searchbar";
import BlockGrid from "./BlockGrid";
import ProviderPanel from "./ProviderPanel";
import { createId } from "../../util";
import unwrapBlocks from "../../util/unwrapBlocks";

const createOption = (value: string) => ({
  value,
  label: value.replace(/[_-]/g, " "),
});

const UnwrappedBlocks: FC<{ blocks: BlockProvider[] }> = ({
  blocks: blocks,
}) => {
  const [unwrapTags, setUnwrapTags] = useState(false);

  const unwrapped = useMemo(
    () => unwrapBlocks(blocks ?? [], !unwrapTags),
    [blocks, unwrapTags]
  );

  const sorted = useMemo(
    () => orderBy(unwrapped, (b) => b.weight, "desc"),
    [unwrapped]
  );
  const size = 100;

  const mods = useMemo(
    () =>
      uniq(unwrapped.map((it) => it.mod?.toLowerCase()))
        .filter(isString)
        .map(createOption),
    [unwrapped]
  );

  const { filter, setFilter, filtered } = useFiltered(sorted);

  return (
    <>
      <FilterBar>
        <Searchbar value={filter} onChange={setFilter} />
        <Checkbox
          id="includeExtras"
          tooltip="Extras are blocks that are generated adjacent to the base"
          value={filter.includeExtras ?? true}
          onChange={(v) => setFilter({ includeExtras: v })}
        >
          Include Extras?
        </Checkbox>
        <Checkbox id="unwrapTags" value={unwrapTags} onChange={setUnwrapTags}>
          Unwrap Tags?
        </Checkbox>
        <Dropdown
          id="mods"
          label="Mods:"
          isClearable
          isMulti
          options={mods}
          value={filter.mods?.map(createOption)}
          onChange={(v) => setFilter({ mods: v.map((it) => it.value) })}
        />
      </FilterBar>
      <Results>{filtered.length} blocks</Results>
      <BlockGrid size={size}>
        {filtered.map((block, i) => (
          <ProviderPanel
            key={createId(block, i)}
            size={size}
            provider={block}
          />
        ))}
      </BlockGrid>
    </>
  );
};

const Results = styled.p`
  font-style: italic;
  text-align: center;
  margin-bottom: 1em;
`;

const FilterBar = styled.section`
  display: grid;
  grid-auto-flow: column;
  height: 5em;
  margin-top: -2em;
  margin-bottom: 2em;
  align-items: center;
  justify-content: start;
  gap: 3em;
`;

export default UnwrappedBlocks;
