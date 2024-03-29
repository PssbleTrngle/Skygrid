import { existsSync, readdirSync, readFileSync, statSync } from "fs";
import { join, parse, resolve } from "path";
import { Block, Named, Tag } from "schema/generated/types";
import DataResolver from "../parser/DataResolver";
import XMLParser from "../parser/XMLParser";

const DATA_DIR = resolve("data");
const BASE_DIR = resolve(".");

function isType(file: string, type: FileSystemHandleKind) {
  if (type === "directory") return statSync(file).isDirectory();
  if (type === "file") return statSync(file).isFile();
  return false;
}

export const serverResolver: DataResolver = {
  exists: async (type, ...path) => {
    const file = resolve(BASE_DIR, ...path);
    if (!existsSync(file)) return false;
    return isType(file, type);
  },
  getContent: async (...path) => {
    const file = resolve(BASE_DIR, ...path);
    if (!existsSync(file)) return null;
    return readFileSync(file).toString();
  },
  getName: async (block) => nameOf(block),
  list: async (type, ...path) => {
    const file = resolve(BASE_DIR, ...path);
    if (!existsSync(file)) return [];
    return readdirSync(file).filter((it) => isType(join(file, it), type));
  },
};

const serverParser = new XMLParser(serverResolver);

function getNamespaces() {
  return readdirSync(DATA_DIR).filter((it) =>
    statSync(resolve(DATA_DIR, it)).isDirectory()
  );
}

export function getDataPath(
  { id, mod }: Named,
  type: string,
  extension: string
) {
  return resolve(DATA_DIR, mod ?? "minecraft", type, `${id}.${extension}`);
}

export function getDataEntries(type: string) {
  const namespaces = getNamespaces();
  return namespaces.flatMap((mod) => {
    const dir = resolve(DATA_DIR, mod, type);
    if (!existsSync(dir)) return [];
    const files = readdirSync(dir).filter((it) =>
      statSync(resolve(dir, it)).isFile()
    );
    return files.map((file) => ({ id: parse(file).name, mod }));
  });
}

const langFile = join("lang", "en_us.json");
const translations = existsSync(langFile)
  ? JSON.parse(readFileSync(langFile).toString())
  : {};

export function nameOf(block: Block | Tag): string | null {
  if (block.__typename === "Tag")
    return (
      translations[`tag.item.${block.mod ?? "minecraft"}.${block.id}`] ?? null
    );
  else
    return (
      translations[`block.${block.mod ?? "minecraft"}.${block.id}`] ?? null
    );
}

export default serverParser;
