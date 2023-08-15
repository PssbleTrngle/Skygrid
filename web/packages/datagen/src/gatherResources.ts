import { createResolver } from "@pssbletrngle/pack-resolver";
import { createDefaultMergers } from "@pssbletrngle/resource-merger";
import fetchSources from "./sourcesFetcher.js";
import { renderUsing } from "@pssbletrngle/assets-renderer";
import { resolve } from "path";
import { cpSync, existsSync } from "fs";

async function run() {
  await fetchSources();

  const include = [
    "data/*/tags/**/*.json",
    "data/*/skygrid/**/*.xml",
    "assets/*/blockstates/**/*.json",
    "assets/*/models/**/*.json",
    "assets/*/textures/**/*.png",
    "assets/*/textures/**/*.png.mcmeta",
  ];

  const tmpDir = "tmp";
  const appDir = resolve("..", "..", "apps", "web");

  if (!existsSync(appDir)) throw new Error(`incorrect app dir '${appDir}'`);

  const resolver = createResolver({ from: "resources", include });
  const mergers = createDefaultMergers({ output: tmpDir });

  await mergers.run(resolver);

  cpSync(resolve(tmpDir, "data"), resolve(appDir, "data"), { recursive: true });
  await renderUsing(tmpDir, { output: resolve(appDir, "public", "icons") }, {});
}

run().catch(console.error);
