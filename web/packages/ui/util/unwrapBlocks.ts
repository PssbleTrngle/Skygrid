import { forPolymorph } from "./polymorphism";
import { groupBy, sumBy } from "lodash-es";
import {
  Block,
  BlockProvider,
  Tag,
  WeightedEntry,
} from "schema/generated/types";

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
        withWeight(unwrapBlocks(p.children, wrappedTags), (w) => w * p.weight),
      Fallback: (p) =>
        withWeight(
          unwrapBlocks(p.children, wrappedTags).slice(0, 1),
          () => p.weight
        ),
      Reference: (p) =>
        p.provider
          ? withWeight(unwrapProvider(p.provider, wrappedTags), () => p.weight)
          : [],
    }) ?? []
  );
}

type Generated<T> = T & { occurrences?: number };

export default function unwrapBlocks<TWrappedTags extends boolean = true>(
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
              unwrapBlocks(it.children, wrappedTags),
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
