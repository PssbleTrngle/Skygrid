import { partition } from "lodash-es";
import { toArray } from "parser";

type Polymorph = {
  enum: Record<string, string>;
  to: string | string[];
  transform: (v: any, i: number, parent?: any) => unknown;
};

const SELF = Symbol();

export default class Polymorpher {
  private polymorphs: Array<Polymorph> = [];

  private polyKey(key: string | undefined, polymorph: Polymorph) {
    if (Array.isArray(polymorph.to)) {
      if (key && polymorph.to.includes(key)) return SELF;
      return null;
    } else {
      return polymorph.to;
    }
  }

  private applyPolymorph(
    polymorph: Polymorph,
    value: any,
    key: string,
    parent: unknown
  ) {
    return Promise.all(
      toArray(value).map((provider, i) =>
        polymorph.transform(
          { __typename: polymorph.enum[key], ...provider },
          i,
          parent
        )
      )
    );
  }

  async applyPolymorphs<R extends object>(input: R, key?: string): Promise<R> {
    if (!input) return input;

    const props = await Promise.all(
      Object.entries(input).map(async ([key, value]) => {
        const recursed = Array.isArray(value)
          ? await Promise.all(value.map((it) => this.applyPolymorphs(it)))
          : typeof value === "object"
          ? await this.applyPolymorphs(value, key)
          : value;
        return { key, value: recursed };
      })
    );

    const parent = props.reduce(
      (o, { key, value }) => ({ ...o, [key]: value }),
      {} as R
    );

    const selfMapper = this.polymorphs.find(
      (it) => this.polyKey(key, it) === SELF
    );

    if (selfMapper) {
      const xmlTags = Object.keys(selfMapper.enum);
      const match = props.filter((it) => xmlTags.includes(it.key));
      const children = await Promise.all(
        match.map(({ key, value }) =>
          this.applyPolymorph(selfMapper, value, key, parent)
        )
      );

      return children.flat() as R;
    } else {
      const mapped = await this.polymorphs.reduce(
        async (initial, polymorph) => {
          const xmlTags = Object.keys(polymorph.enum);

          const polyKey = this.polyKey(key, polymorph);
          if (!polyKey) return initial;

          const [match, noMatch] = partition(await initial, ({ key }) =>
            xmlTags.includes(key)
          );

          const children = await Promise.all(
            match.map(({ key, value }) =>
              this.applyPolymorph(polymorph, value, key, parent)
            )
          );

          return [...noMatch, { key: polyKey, value: children.flat() }];
        },
        Promise.resolve(props)
      );

      return mapped.reduce(
        (o, { key, value }) => ({ ...o, [key]: value }),
        {} as R
      );
    }
  }

  register<T extends object>(
    to: string | string[],
    enm: Record<string, string>,
    transform: (v: T, i: number, parent?: T) => T | Promise<T> = (v) => v
  ) {
    this.polymorphs.push({ to, enum: enm, transform });
  }
}
