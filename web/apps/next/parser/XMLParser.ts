import * as console from "console";
import {
  Block,
  BlockProvider,
  DimensionConfig,
  Extra,
  Filter,
  Named,
  Preset,
  Reference,
  Tag,
  TagFilter,
} from "schema/generated/types";
import { exists } from "ui/util";
import { forPolymorph, Typename } from "ui/util/polymorphism";
import { Parser } from "xml2js";
import DataResolver from "./DataResolver";
import Polymorpher from "./Polymorpher";

const NUMERICS = ["weight", "probability", "offset", "x", "y", "z"];

export enum ResourceType {
  CONFIG = "dimensions",
  PRESET = "presets",
}

interface ResourceTypes {
  [ResourceType.CONFIG]: DimensionConfig;
  [ResourceType.PRESET]: Preset;
}

export type Resource<T extends ResourceType> = ResourceTypes[T];

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
  return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value);
}

export type TagValue =
  | string
  | {
      id: string;
      required?: boolean;
    };

export interface TagDefinition {
  replace?: boolean;
  values: TagValue[];
}

const ProviderType: Record<string, Typename<BlockProvider>> = {
  block: "Block",
  tag: "Tag",
  list: "BlockList",
  fallback: "Fallback",
  reference: "Reference",
};

const ExtrasType: Record<string, Typename<Extra>> = {
  cardinal: "CardinalExtra",
  side: "SideExtra",
  offset: "OffsetExtra",
};

const FiltersType: Record<string, Typename<Filter>> = {
  mod: "ModFilter",
  tag: "TagFilter",
  name: "NameFilter",
};

type RawDimensionConfig = Omit<DimensionConfig, "blocks"> & {
  blocks: {
    children: DimensionConfig["blocks"];
  };
};

type RawPreset = {
  children: [BlockProvider];
};

export default class XMLParser {
  private polymorpher = new Polymorpher();

  constructor(private resolver: DataResolver) {
    this.polymorpher.register<BlockProvider>(
      "children",
      ProviderType,
      async (provider, index, parent) => {
        const weight = provider.weight ?? 1;
        //const uuid = provider.type === ProviderType.BLOCK ? nanoid(8) : `${provider.type}-${index + 1}`

        const except = [...(parent?.except ?? []), ...(provider.except ?? [])];

        const extra = await forPolymorph<BlockProvider, Promise<{}>>(provider, {
          Tag: async (p) => ({
            name: await this.resolver.getName(p),
            matches: await this.getBlocksFor({ ...p, except }),
          }),
          Block: (b) => this.extendBlock(b),
          Reference: async (p) => ({ provider: await this.getPreset(p) }),
        });

        return { ...provider, ...extra, weight };
      }
    );

    this.polymorpher.register<Extra>("extras", ExtrasType, async (e) => ({
      ...e,
      probability: e.probability ?? 1,
    }));

    this.polymorpher.register<Filter>(["except"], FiltersType);
  }

  async getResources(type: ResourceType) {
    const namespaces = await this.resolver.list("directory", "data");
    const resources = await Promise.all(
      namespaces.map(async (mod) => {
        const dir = ["data", mod, "skygrid", type];
        if (!(await this.resolver.exists("directory", ...dir))) return [];
        const files = await this.resolver.list("file", ...dir);
        return Promise.all(
          files
            .filter((f) => f.endsWith("xml"))
            .map(async (file) => ({
              mod,
              id: file.substring(0, file.length - 4),
              lastModified:
                (await this.resolver.lastModified?.(...dir, file)) ?? null,
            }))
        );
      })
    );
    return resources.flat();
  }

  async getConfig(key: Named): Promise<DimensionConfig | null> {
    const path = this.getDataPath(key, `skygrid/${ResourceType.CONFIG}`, "xml");
    const raw = await this.parseFile<RawDimensionConfig>(...path);
    if (!raw) return null;
    return { ...raw, blocks: raw.blocks.children };
  }

  async getPreset(reference: Reference): Promise<BlockProvider | null> {
    const path = this.getDataPath(
      reference,
      `skygrid/${ResourceType.PRESET}`,
      "xml"
    );
    const parsed = await this.parseFile<RawPreset>(...path);
    return parsed?.children[0] ?? null;
  }

  async getIcon(block: Named) {
    const mod = block.mod ?? "minecraft";
    const icon = `icons/${mod}/${block.id}.png`;
    if (await this.resolver.exists("file", "public", icon)) {
      return `/${icon}`;
    }
    return null;
  }

  async extendBlock(block: Block) {
    return {
      mod: block.mod ?? "minecraft",
      name: await this.resolver.getName(block),
      icon: await this.getIcon(block),
    };
  }

  private getDataPath({ id, mod }: Named, type: string, extension: string) {
    return ["data", mod ?? "minecraft", type, `${id}.${extension}`];
  }

  async tagDefinition(tag: Pick<Tag, "id" | "mod" | "except">) {
    const path = this.getDataPath(tag, "tags/blocks", "json");
    const content = await this.resolver.getContent(...path);
    if (!content) {
      console.warn(
        `Missing tag definition for #${tag.mod ?? "minecraft"}:${tag.id}`
      );
      return null;
    }
    try {
      return JSON.parse(content) as TagDefinition;
    } catch {
      console.warn(
        `Failed to parse tag for #${tag.mod ?? "minecraft"}:${tag.id}`
      );
      return null;
    }
  }

  async getBlocksFor(
    tag: Pick<Tag, "id" | "mod" | "except">
  ): Promise<Block[]> {
    const definition = await this.tagDefinition(tag);
    if (!definition) return [];

    const filters: Array<(block: Block) => boolean> = [];

    if (tag.except?.length) {
      const tagFilters = await Promise.all(
        tag.except
          .filter((it) => it.__typename === "TagFilter")
          .map((it: TagFilter) => this.getBlocksFor(it))
      );

      tag.except.forEach((it) => {
        if (it.__typename === "NameFilter") {
          filters.push((b) => `${b.mod}${b.id}`.includes(it.pattern));
        } else if (it.__typename === "ModFilter") {
          filters.push((b) => {
            return b.mod === it.id;
          });
        }
      });

      tagFilters.forEach((matches) =>
        filters.push((a) =>
          matches.some((b) => a.id === b.id && a.mod === b.mod)
        )
      );
    }

    const resolved = await Promise.all(
      definition.values.map(async (value) => {
        const tagID = typeof value === "string" ? value : value.id;
        const [mod, id] = tagID.split(":");
        if (tagID.startsWith("#"))
          return this.getBlocksFor({ mod: mod.substring(1), id });
        else return [{ id, mod, weight: 1 }];
      })
    );

    const extended = await Promise.all(
      resolved.flat().map<Promise<Block>>(async (b) => ({
        ...b,
        ...(await this.extendBlock(b)),
      }))
    );

    return (
      extended.filter((b) => !filters.some((it) => it(b))).filter(exists) ?? []
    );
  }

  private async parseFile<T extends object>(...path: string[]) {
    const content = await this.resolver.getContent(...path);
    if (!content) return null;
    return this.parse<T>(content);
  }

  private async parse<T extends object>(input: string) {
    const parser = new Parser({
      explicitRoot: false,
      mergeAttrs: true,
      explicitArray: false,
      attrValueProcessors: [
        modify(NUMERICS, Number.parseFloat),
        modify(["id"], (it) => (it.startsWith("#") ? it.substring(1) : it)),
      ],
    });

    const parsed: T = await parser.parseStringPromise(input);

    return this.polymorpher.applyPolymorphs(parsed);
  }
}
