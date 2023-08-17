import { renderUsing } from "@pssbletrngle/assets-renderer";
import { createResolver } from "@pssbletrngle/pack-resolver";
import { createDefaultMergers } from "@pssbletrngle/resource-merger";
import { cpSync, existsSync } from "fs";
import { resolve } from "path";
import mergeLangFiles from "./langMerger.js";
import fetchSources from "./sourcesFetcher.js";

async function run() {
  await fetchSources();

  const include = [
    "data/*/tags/**/*.json",
    "data/*/skygrid/**/*.xml",
    "assets/*/blockstates/**/*.json",
    "assets/*/lang/en_us.json",
    "assets/*/models/**/*.json",
    "assets/*/textures/**/*.png",
    "assets/*/textures/**/*.png.mcmeta",
  ];

  const tmpDir = "tmp";
  const appDir = resolve("..", "..", "apps", "next");

  if (!existsSync(appDir)) throw new Error(`incorrect app dir '${appDir}'`);

  const resolver = createResolver({ from: "resources", include });
  const mergers = createDefaultMergers({ output: tmpDir, overwrite: true });

  await mergers.run(resolver);

  cpSync(resolve(tmpDir, "data"), resolve(appDir, "data"), { recursive: true });

  mergeLangFiles(resolve(tmpDir, "assets"), resolve(appDir, "lang"));

  await renderUsing(tmpDir, { output: resolve(appDir, "public", "icons") }, {});
}

run().catch((e) => {
  console.error(e);
  process.exit(-1);
});
