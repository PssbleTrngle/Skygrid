import { UrlFetcher } from "./index.js";
import { Source } from "../sources.js";
import axios, { AxiosInstance } from "axios";

export default class AnyUrlFetcher extends UrlFetcher {
  private api = axios.create({ responseType: "stream" });

  getApi(): AxiosInstance {
    return this.api;
  }

  async getUrl(source: Source) {
    if (!source.url) throw new Error("URL missing");
    return source.url;
  }
}
