import { omit, sumBy } from "lodash-es";
import { useRouter } from "../../context/router";
import { FC, useCallback, useEffect, useMemo } from "react";
import styled from "styled-components";
import { BlockList, BlockProvider, Fallback } from "schema/generated/types";
import { forPolymorph, Mapper } from "../../util/polymorphism";
import { createId, exists } from "../../util";
import Breadcrumbs from "../Breadcrumbs";
import BlockGrid from "./BlockGrid";
import ProviderPanel from "./ProviderPanel";

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

const HierarchicalBlocks: FC<{ blocks: BlockProvider[] }> = ({ blocks }) => {
  const router = useRouter();
  const path = useMemo(
    () =>
      router.query.path
        ?.toString()
        .split("/")
        .map((it) => it.trim())
        .filter(exists) ?? [],
    [router.query]
  );

  const shown = useMemo(() => {
    const list = path.length ? findRecursive(path, blocks) : blocks;
    const totalWeight = sumBy(list, (it) => it.weight);
    return list?.map((p) => ({ ...p, weight: p.weight / totalWeight }));
  }, [blocks, path]);

  useEffect(() => {
    if (shown === null) router.push({ query: omit(router.query, "to") });
  }, [router, shown]);

  const navigate = useCallback(
    (to: string[]) => {
      if (to.length)
        router.push({ query: { ...router.query, path: to.join("/") } });
      else router.push({ query: omit(router.query, "path") });
    },
    [router]
  );

  const click = useCallback(
    (provider: BlockProvider, index: number) => () => {
      if (Object.keys(recurseStep).includes(provider.__typename!!)) {
        const id = provider.name ?? createId(provider, index);
        navigate([...path, id]);
      }
    },
    [navigate, path]
  );

  return (
    <Style>
      {path && (
        <Breadcrumbs
          root={router.query.config as string}
          crumbs={path}
          onClick={navigate}
        />
      )}
      <BlockGrid size={200}>
        {shown?.map((provider, i) => (
          <ProviderPanel
            key={createId(provider, i)}
            size={200}
            provider={provider}
            onClick={click(provider, i)}
          />
        ))}
      </BlockGrid>
    </Style>
  );
};

const Style = styled.div``;

export default HierarchicalBlocks;
