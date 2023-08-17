import { debounce } from "lodash-es";
import { mix } from "polished";
import {
  ChangeEvent,
  Dispatch,
  FC,
  useCallback,
  useMemo,
  useState,
} from "react";
import styled from "styled-components";
import { exists } from "../../util";
import { forPolymorph } from "../../util/polymorphism";
import { BlockProvider, Named } from "schema/generated/types";

interface Filter {
  mods: string[];
  text?: string;
  includeExtras?: boolean;
}

function searchTargets(provider: BlockProvider): string[] {
  return [
    provider.name,
    ...(forPolymorph<BlockProvider, Array<string | null | undefined>>(
      provider,
      {
        Block: (p) => [p.id, p.mod],
        Tag: (p) => [p.id, p.mod],
        BlockList: (p) => p.children.flatMap(searchTargets),
        Fallback: (p) => p.children && searchTargets(p.children[0]),
        Reference: (p) => (p.provider ? searchTargets(p.provider) : []),
      }
    ) ?? []),
  ].filter(exists);
}

const DEFAULT_FILTER: Filter = { mods: ["minecraft", "forge", "fabric"] };

export function useFiltered<B extends BlockProvider & Partial<Named>>(
  unfiltered: B[]
) {
  const [filter, setInstant] = useState<Filter>(DEFAULT_FILTER);
  const [lazyFilter, setLazy] = useState<Filter>(DEFAULT_FILTER);

  const filtered = useMemo(
    () =>
      unfiltered
        .filter((p) => lazyFilter.includeExtras !== false || !p.extra)
        .filter(
          (p) =>
            lazyFilter.mods.length === 0 ||
            lazyFilter.mods.some((it) => it === (p.mod ?? "minecraft"))
        )
        .filter((provider) => {
          const search = lazyFilter.text?.toLocaleLowerCase();
          if (!search) return true;

          return searchTargets(provider)
            .map((it) => it.toLowerCase())
            .some((it) => it.includes(search));
        }),
    [unfiltered, lazyFilter]
  );

  const debounced = useMemo(() => debounce(setLazy, 300), [setLazy]);

  const setFilter = useCallback(
    (partial: Partial<Filter>) => {
      setInstant((it) => {
        const newFilter = { ...it, ...partial };
        debounced(newFilter);
        return newFilter;
      });
    },
    [setInstant, debounced]
  );

  return { filter, setFilter, filtered };
}

const Searchbar: FC<{
  value: Filter;
  onChange: Dispatch<Partial<Filter>>;
}> = ({ value, onChange }) => {
  const callback = useCallback(
    (e: ChangeEvent<HTMLInputElement>) => onChange({ text: e.target.value }),
    [onChange]
  );
  return (
    <Style placeholder="Search" onChange={callback} value={value.text ?? ""} />
  );
};

const Style = styled.input`
  border: none;
  min-width: 400px;

  padding: 0.5em 1em;
  color: ${(p) => p.theme.text};

  outline: 1px ${(p) => mix(0.8, p.theme.bg, p.theme.text)} solid;

  &:focus-visible {
    outline: 2px ${(p) => p.theme.accent} solid;
  }
`;

export default Searchbar;
