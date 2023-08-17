import { UrlFetcher } from "./index.js";
import { Source } from "../sources.js";
import axios, { AxiosInstance } from "axios";
import getEnv from "../env.js";

function replaceEnv(string: string) {
  const matches = string.match(/(\$\w+)($|\s)/g) ?? [];
  return matches
    .map((it) => it.slice(1))
    .reduce((s, key) => {
      const env = getEnv(key);
      return s.replace(s, env);
    }, string);
}

export default class AnyUrlFetcher extends UrlFetcher {
  private api = axios.create({ responseType: "stream" });

  getApi(): AxiosInstance {
    return this.api;
  }

  async getUrl(source: Source) {
    if (!source.url) throw new Error("URL missing");
    return replaceEnv(source.url);
  }
}
