import { forPolymorph, Mapper } from "./polymorphism";
import { createId, exists } from "./index";
import { BlockList, BlockProvider, Fallback } from "schema/generated/types";
import { ParsedUrlQuery } from "querystring";
import { useRouter } from "../context/router";
import { SetStateAction, useCallback, useEffect, useMemo } from "react";
import { omit, sumBy } from "lodash-es";

function recurseList(it: BlockList | Fallback) {
  return (rest: string[]) => {
    if (rest.length === 0) return it.children;
    return findRecursive(rest, it.children);
  };
}

const recurseStep: Mapper<
  BlockProvider,
  (rest: string[]) => BlockProvider[] | null
> = {
  BlockList: recurseList,
  Fallback: recurseList,
  Reference: (it) => (rest) => {
    if (!it.provider) return [];
    return findRecursive([createId(it.provider), ...rest], [it.provider]);
  },
};

function splitPath(query: ParsedUrlQuery) {
  return (
    query.path
      ?.toString()
      .split("/")
      .map((it) => it.trim())
      .filter(exists) ?? []
  );
}

export function useProviderPath() {
  const router = useRouter();
  return useMemo(() => splitPath(router.query), [router.query]);
}

export function useShownFilter(blocks: BlockProvider[], path: string[]) {
  const router = useRouter();

  const shown = useMemo(() => {
    const list = path.length ? findRecursive(path, blocks) : blocks;
    const totalWeight = sumBy(list, (it) => it.weight);
    if (!list) return null;
    return list.map<BlockProvider>((p) => ({
      ...p,
      weight: p.weight / totalWeight,
    }));
  }, [blocks, path]);

  useEffect(() => {
    if (shown === null) router.push({ query: omit(router.query, "to") });
  }, [router, shown]);

  return useMemo(() => shown ?? [], [shown]);
}

export function canBeRecursed(provider: BlockProvider) {
  return Object.keys(recurseStep).includes(provider.__typename!!);
}

export function useNavigateToPath() {
  const router = useRouter();
  const path = useProviderPath();
  return useCallback(
    (to: SetStateAction<string[]>) => {
      const resolvedTo = typeof to === "function" ? to(path) : to;
      if (to.length)
        router.push({ query: { ...router.query, path: resolvedTo.join("/") } });
      else router.push({ query: omit(router.query, "path") });
    },
    [router, path]
  );
}

function findRecursive(
  path: string[],
  blocks: BlockProvider[]
): BlockProvider[] | null {
  const [search, ...rest] = path;
  if (!search) return null;
  const match = blocks.find(
    (b, i) => createId(b, i) === search || b.name === search
  );
  if (match) return forPolymorph(match, recurseStep)?.(rest) ?? null;
  return null;
}
