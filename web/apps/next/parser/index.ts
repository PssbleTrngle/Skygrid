import { exists } from "ui/util";

export function toArray<T>(value?: T | T[] | null) {
  if (!exists(value)) return [];
  if (Array.isArray(value)) return value;
  if (typeof value === "object" && "0" in value!)
    return Object.values(value) as T[];
  return [value];
}
