import { exists } from "ui/util";

export function toArray<T>(value?: T | T[]) {
  if (!exists(value)) return [];
  return Array.isArray(value) ? value : [value];
}
