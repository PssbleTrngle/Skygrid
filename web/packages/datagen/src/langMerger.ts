import {
  existsSync,
  mkdirSync,
  readdirSync,
  readFileSync,
  writeFileSync,
} from "fs";
import { resolve } from "path";
import json from "json5";

export default function mergeLangFiles(assetsDir: string, to: string) {
  const namespaces = readdirSync(assetsDir);
  const langFiles = namespaces
    .map((it) => resolve(assetsDir, it, "lang", "en_us.json"))
    .filter((it) => existsSync(it));

  const merged = langFiles.reduce((previous, file) => {
    const raw = readFileSync(file).toString();
    const parsed = json.parse(raw);
    return { ...previous, ...parsed };
  }, {});

  if (!existsSync(to)) mkdirSync(to, { recursive: true });
  writeFileSync(resolve(to, "en_us.json"), JSON.stringify(merged, null, 2));
}
