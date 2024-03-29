import { Block, Tag } from "schema/generated/types";

export default interface DataResolver {
  exists(type: FileSystemHandleKind, ...path: string[]): Promise<boolean>;
  getContent(...path: string[]): Promise<string | null>;
  getName(block: Block | Tag): Promise<string | null>;
  list(type: FileSystemHandleKind, ...path: string[]): Promise<string[]>;
  lastModified?: (...path: string[]) => Promise<number>;
}
