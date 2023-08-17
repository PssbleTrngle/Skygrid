import Fetcher from "./index.js";
import { Source } from "../sources.js";
import { cpSync, existsSync } from "fs";

export default class FileFetcher extends Fetcher {
  async fetch(source: Source, output: string) {
    if (!source.path) throw new Error("File path missing");
    if (!existsSync(source.path)) throw new Error("File not found");

    cpSync(source.path, output, {
      recursive: true,
    });
  }
}
