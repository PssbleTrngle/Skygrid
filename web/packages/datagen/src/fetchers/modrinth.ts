import axios, { AxiosInstance } from "axios";
import getEnv from "../env.js";
import { UrlFetcher } from "./index.js";
import { Source } from "../sources.js";

interface ModrinthVersion {
  files: Array<{
    url: string;
  }>;
}

export default class ModrinthFetcher extends UrlFetcher {
  private api = axios.create({
    baseURL: "https://api.modrinth.com/v2",
    responseType: "json",
    headers: {
      "User-Agent": "PssbleTrngle/Skygrid (skygrid.somethingcatchy.net)",
      Authorization: getEnv("MODRINTH_TOKEN"),
    },
  });

  getApi(): AxiosInstance {
    return this.api;
  }

  async getUrl(source: Source) {
    if (!source.file) throw new Error("File id missing");

    const version = await this.api.get<ModrinthVersion>(
      `/version/${source.file}`
    );

    return version.data.files[0].url;
  }
}
