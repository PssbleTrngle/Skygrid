import { partition } from "lodash-es";
import { toArray } from "parser";

export default class Polymorpher {
  private polymorphs: Array<{
    enum: Record<string, string>;
    to: string;
    transform: (v: any, i: number, parent?: any) => unknown;
  }> = [];

  async applyPolymorphs<R extends object>(input: R): Promise<R> {
    if (!input) return input;

    const props = await Promise.all(
      Object.entries(input).map(async ([key, value]) => {
        const recursed = Array.isArray(value)
          ? await Promise.all(value.map((it) => this.applyPolymorphs(it)))
          : typeof value === "object"
          ? await this.applyPolymorphs(value)
          : value;
        return { key, value: recursed };
      })
    );

    const parent = props.reduce(
      (o, { key, value }) => ({ ...o, [key]: value }),
      {} as R
    );

    const mapped = await this.polymorphs.reduce(async (initial, polymorph) => {
      const keys = Object.values(polymorph.enum);

      const [match, noMatch] = partition(await initial, ({ key }) =>
        keys.includes(key)
      );

      const children = await Promise.all(
        match
          .map(({ key, value }) => ({ key, value: toArray(value) }))
          .map(({ key, value }) =>
            Promise.all(
              value.map((provider, i) =>
                polymorph.transform({ __typename: key, ...provider }, i, parent)
              )
            )
          )
      );

      return [...noMatch, { key: polymorph.to, value: children.flat() }];
    }, Promise.resolve(props));

    return mapped.reduce(
      (o, { key, value }) => ({ ...o, [key]: value }),
      {} as R
    );
  }

  register<T extends object>(
    to: string,
    enm: Record<string, string>,
    transform: (v: T, i: number, parent?: T) => T | Promise<T> = (v) => v
  ) {
    this.polymorphs.push({ to, enum: enm, transform });
  }
}
