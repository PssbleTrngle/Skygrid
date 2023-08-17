import { existsSync, readFileSync, statSync } from "fs";
import { extname } from "path";

export interface Source {
  name: string;
  url?: string;
  path?: string;
  type?: string;
  project?: number;
  file?: number | string;
}

function parseJson(file: string): Source[] {
  const content = readFileSync(file).toString();
  return JSON.parse(content);
}

function parseFile(file: string): Source[] {
  const extension = extname(file);
  switch (extension) {
    case ".json":
      return parseJson(file);
    case ".jar":
    case ".zip":
      return [{ type: "file", path: file, name: file }];
    default:
      throw new Error(`Unknown source type with extension '${extension}'`);
  }
}

export default function parseSources(from: string): Source[] {
  if (existsSync(from)) {
    const info = statSync(from);

    if (info.isFile()) {
      return parseFile(from);
    } else {
      throw new Error("Directory fetchers not yet supported");
    }
  } else {
    throw new Error(`sources at ${from} could not be found`);
  }
}
