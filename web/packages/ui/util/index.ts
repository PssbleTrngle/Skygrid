import { BlockProvider } from "schema/generated/types";

export function exists<T>(value: T | undefined | null): value is T {
  return (value ?? null) !== null;
}

export function createId(block: BlockProvider, index: number = 0): string {
  if (block.__typename === "Reference") return block.id;
  return block.name ?? `${block.__typename?.toLowerCase()}-${index}`;
}
