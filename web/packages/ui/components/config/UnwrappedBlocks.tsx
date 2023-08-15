import Dropdown from "../inputs/Dropdown";
import { groupBy, isString, orderBy, sumBy, uniq } from "lodash-es";
import { FC, useMemo, useState } from "react";
import styled from "styled-components";
import {
  Block,
  BlockProvider,
  Tag,
  WeightedEntry,
} from "schema/generated/types";
import { forPolymorph } from "../../util/polymorphism";
import Checkbox from "../inputs/Checkbox";
import Searchbar, { useFiltered } from "../inputs/Searchbar";
import BlockGrid from "./BlockGrid";
import ProviderPanel from "./ProviderPanel";
import { createId } from "../../util";

const wrap = (value: string) => ({ value, label: value.replace(/[_-]/g, " ") });

const UnwrappedBlocks: FC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
  const [unwrapTags, setUnwrapTags] = useState(false);

  const unwrapped = useMemo(
    () => unwrap(blocks, !unwrapTags),
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
        .map(wrap),
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
        <Checkbox id="unwrapTsgs" value={unwrapTags} onChange={setUnwrapTags}>
          Unwrap Tags?
        </Checkbox>
        <Dropdown
          id="mods"
          label="Mods:"
          isClearable
          isMulti
          options={mods}
          value={filter.mods?.map(wrap)}
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
  margin-bottom: 2em;
  align-items: center;
  justify-content: start;
  gap: 3em;
`;

function withWeight<T extends Required<WeightedEntry>>(
  blocks: T[],
  func: (w: number) => number = (w) => w
): T[] {
  return blocks.map((b) => ({ ...b, weight: func(b.weight) }));
}

function unwrapProvider(
  provider: BlockProvider,
  wrappedTags?: boolean
): BlockProvider[] {
  return (
    forPolymorph(provider, {
      Block: (p) => [p as BlockProvider],
      Tag: (p) =>
        wrappedTags ? [p] : withWeight(p.matches, (w) => w / p.matches.length),
      BlockList: (p) =>
        withWeight(unwrap(p.children, wrappedTags), (w) => w * p.weight),
      Fallback: (p) =>
        withWeight(unwrap(p.children, wrappedTags).slice(0, 1), () => p.weight),
      Reference: (p) =>
        p.provider
          ? withWeight(unwrapProvider(p.provider, wrappedTags), () => p.weight)
          : [],
    }) ?? []
  );
}

type Generated<T> = T & { occurrences?: number };

export function unwrap<TWrappedTags extends boolean = true>(
  providers: BlockProvider[],
  wrappedTags?: TWrappedTags
): Array<
  TWrappedTags extends true
    ? Generated<Tag> | Generated<Block>
    : Generated<Block>
> {
  const unwrapped = providers.flatMap((p) =>
    unwrapProvider(p, wrappedTags)
  ) as Generated<Block>[];
  const total = sumBy(unwrapped, (w) => w.weight);
  const normalized = unwrapped
    .map((p) => ({ ...p, weight: p.weight / total }))
    .flatMap(({ extras, ...provider }) => {
      const unwrappedExtras = extras
        ? extras.flatMap((it) =>
            withWeight(
              unwrap(it.children, wrappedTags),
              (w) => w * it.probability
            )
          )
        : [];

      return [
        { ...provider, extra: provider.extra ?? false },
        ...withWeight(unwrappedExtras, (w) => w * provider.weight).map((e) => ({
          ...e,
          extra: true,
        })),
      ];
    });

  return Object.values(groupBy(normalized, (n) => `${n.mod}:${n.id}`)).map(
    (occurrences, i) => ({
      ...occurrences[0],
      occurrences: occurrences.length,
      weight: sumBy(occurrences, (it) => it.weight),
      __typename: occurrences[0].__typename ?? "Block",
    })
  );
}

export default UnwrappedBlocks;
