import { existsSync, mkdirSync } from "fs";
import { join } from "path";
import parseSources, { Source } from "./sources.js";
import ModrinthFetcher from "./fetchers/modrinth.js";
import CurseforgeFetcher from "./fetchers/curseforge.js";
import FileFetcher from "./fetchers/file.js";
import Fetcher from "./fetchers/index.js";
import AnyUrlFetcher from "./fetchers/url.js";
import getEnv from "./env.js";

function replaceEnv(string: string) {
  const matches = string.match(/(\$\w+)($|\s)/g) ?? [];
  return matches
    .map((it) => it.slice(1))
    .reduce((s, key) => {
      const env = getEnv(key);
      return s.replace(s, env);
    }, string);
}

function createFetchers() {
  const fetchers: Record<string, new () => Fetcher> = {
    modrinth: ModrinthFetcher,
    curseforge: CurseforgeFetcher,
    file: FileFetcher,
    url: AnyUrlFetcher,
  };

  const cachedFetchers = new Map<string, Fetcher>();

  return (type = "url"): Fetcher => {
    if (type in fetchers) {
      const cached = cachedFetchers.get(type);
      if (cached) return cached;

      const fetcher = new fetchers[type]();
      cachedFetchers.set(type, fetcher);
      return fetcher;
    } else {
      throw new Error(`Unknown source type '${type}'`);
    }
  };
}

const getFetcher = createFetchers();

async function fetchSource(
  source: Source,
  to: string,
  useCached: boolean
): Promise<void> {
  const output = join(to, source.name);

  if (useCached && (existsSync(output) || existsSync(`${output}.jar`))) {
    console.log(`Using cached ${source.name}`);
    return;
  }

  console.log(`Fetching ${source.name}`);

  try {
    const fetcher = getFetcher(source.type);
    await fetcher.fetch(source, output);
  } catch (e) {
    console.error(`Error fetching ${source.name}:`);
    throw e;
  }

  console.log(`Successfully fetched ${source.name}`);

  // wait because of a race condition
  await new Promise((res) => setTimeout(res, 500));
}

export default async function fetchSources() {
  const sources = parseSources("sources.json");
  const to = "resources";
  console.group(`Fetching ${sources.length} sources`);
  if (!existsSync(to)) mkdirSync(to, { recursive: true });
  await Promise.all(sources.map((it) => fetchSource(it, to, true)));
  console.groupEnd();
}
