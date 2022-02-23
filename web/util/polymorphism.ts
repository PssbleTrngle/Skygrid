import { partition } from 'lodash'

export function toArray<T>(t?: T | T[]) {
   if (!t) return []
   return Array.isArray(t) ? t : [t]
}

export type Polymorph<Morph, Type extends keyof Morph = keyof Morph> = Morph[Type] & {
   type: Type
}

type MorphMap<Morph, R> = {
   [Type in keyof Morph]?: (v: Polymorph<Morph, Type>) => R
}

export function forPolymorph<Morph, R>(
   value: Polymorph<Morph>,
   map: MorphMap<Morph, R>
): R | undefined {
   const func = map[value.type]
   return func?.(value as Polymorph<any>)
}

export class Polymorpher {
   private polymorphs: Array<{
      enum: Record<string, string>
      to: string
      transform: (v: any) => unknown
   }> = []

   async applyPolymorphs<R extends object>(input: R): Promise<R> {
      if (!input) return input

      const props = await Promise.all(
         Object.entries(input).map(async ([key, value]) => {
            const recursed = Array.isArray(value)
               ? await Promise.all(value.map(it => this.applyPolymorphs(it)))
               : typeof value === 'object'
               ? await this.applyPolymorphs(value)
               : value
            return { key, value: recursed }
         })
      )

      const mapped = await this.polymorphs.reduce(async (value, polymorph) => {
         const keys = Object.values(polymorph.enum)

         const [match, noMatch] = partition(await value, ({ key }) => keys.includes(key))

         const children = await Promise.all(
            match
               .map(({ key, value }) => ({ key, value: toArray(value) }))
               .map(({ key, value }) =>
                  Promise.all(
                     value.map(provider => polymorph.transform({ type: key, ...provider }))
                  )
               )
         )

         return [...noMatch, { key: polymorph.to, value: children.flat() }]
      }, Promise.resolve(props))

      return mapped.reduce((o, { key, value }) => ({ ...o, [key]: value }), {} as R)
   }

   register<T extends object>(
      to: string,
      enm: Record<string, string>,
      transform: (v: T) => T | Promise<T> = v => v
   ) {
      this.polymorphs.push({ to, enum: enm, transform })
   }
}
