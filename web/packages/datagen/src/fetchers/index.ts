import { Source } from "../sources.js";
import { createWriteStream } from "fs";
import { AxiosInstance } from "axios";

export default abstract class Fetcher {
  abstract fetch(source: Source, output: string): Promise<void>;
}

export abstract class UrlFetcher extends Fetcher {
  async fetch(source: Source, output: string) {
    const url = await this.getUrl(source);
    const file = await this.getApi().get(url, {
      responseType: "stream",
    });
    file.data.pipe(createWriteStream(output + ".jar"));
  }

  abstract getApi(): AxiosInstance;

  abstract getUrl(source: Source): Promise<string>;
}
