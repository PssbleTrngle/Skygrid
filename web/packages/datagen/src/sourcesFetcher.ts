import axios from "axios";
import { cpSync, createWriteStream, existsSync, mkdirSync } from "fs";
import { basename, join } from "path";
import parseSources, { Source } from "./sources.js";

function getEnv(key: string) {
  const env = process.env[key];
  if (!env) throw new Error(`Environment Variable '${key}' missing`);
  return env;
}

const request = axios.create({ responseType: "stream" });

const curseforge = axios.create({
  baseURL: "https://api.curseforge.com/v1",
  responseType: "json",
  headers: {
    Accept: "application/json",
    "x-api-key": getEnv("CURSEFORGE_TOKEN"),
  },
});

const modrinthHeaders = {
  "User-Agent": "PssbleTrngle/Skygrid (skygrid.somethingcatchy.net)",
  Authorization: getEnv("MODRINTH_TOKEN"),
};

const modrinth = axios.create({
  baseURL: "https://api.modrinth.com/v2",
  responseType: "json",
  headers: {
    ...modrinthHeaders,
    Accept: "application/json",
  },
});

function replaceEnv(string: string) {
  const matches = string.match(/(\$\w+)($|\s)/g) ?? [];
  return matches
    .map((it) => it.slice(1))
    .reduce((s, key) => {
      const env = getEnv(key);
      return s.replace(s, env);
    }, string);
}

interface ModrinthVersion {
  files: Array<{
    url: string;
  }>;
}

async function fetchSource(
  source: Source,
  to: string,
  useCached: boolean
): Promise<void> {
  const outJar = join(to, `${source.name}.jar`);

  if (useCached && existsSync(outJar)) {
    console.log(`Using cached ${source.name}`);
    return;
  }

  console.log(`Fetching ${source.name}`);

  try {
    if (source.type === "modrinth") {
      if (!source.file) throw new Error("File id missing");

      const version = await modrinth.get<ModrinthVersion>(
        `/version/${source.file}`
      );

      const file = await request.get(version.data.files[0].url, {
        headers: modrinthHeaders,
      });

      file.data.pipe(createWriteStream(outJar));
    } else if (source.type === "curseforge") {
      if (!source.project) throw new Error("Project id missing");
      if (!source.file) throw new Error("File id missing");

      const download = await curseforge.get<{ data: string }>(
        `/mods/${source.project}/files/${source.file}/download-url`
      );

      const file = await request.get(download.data.data);
      file.data.pipe(createWriteStream(outJar));
    } else if (source.type === "file") {
      if (!source.path) throw new Error("File path missing");
      if (!existsSync(source.path)) throw new Error("File not found");

      cpSync(source.path, join(to, basename(source.path)), { recursive: true });
    } else if (source.url) {
      const url = replaceEnv(source.url);

      const { data } = await request.get(url);
      data.pipe(createWriteStream(outJar));
    }
  } catch (e) {
    console.error(`Error fetching ${source.name}:`);
    throw e;
  }

  console.log(`Successfully fetched ${source.name}`);
}

export default async function fetchSources() {
  const sources = parseSources("sources.json");
  const to = "resources";
  console.group(`Fetching ${sources.length} sources`);
  if (!existsSync(to)) mkdirSync(to, { recursive: true });
  await Promise.all(sources.map((it) => fetchSource(it, to, true)));
  console.groupEnd();
}
