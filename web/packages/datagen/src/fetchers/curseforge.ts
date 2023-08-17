import axios, { AxiosInstance } from "axios";
import getEnv from "../env.js";
import { UrlFetcher } from "./index.js";
import { Source } from "../sources.js";

export default class CurseforgeFetcher extends UrlFetcher {
  private api = axios.create({
    baseURL: "https://api.curseforge.com/v1",
    headers: {
      Accept: "application/json",
      "x-api-key": getEnv("CURSEFORGE_TOKEN"),
    },
  });

  getApi(): AxiosInstance {
    return this.api;
  }

  async getUrl(source: Source) {
    if (!source.project) throw new Error("Project id missing");
    if (!source.file) throw new Error("File id missing");

    const download = await this.api.get<{ data: string }>(
      `/mods/${source.project}/files/${source.file}/download-url`
    );

    return download.data.data;
  }
}
