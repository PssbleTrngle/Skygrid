import { partition } from 'lodash'

const POLYMORPHS: Array<{
   enum: Record<string, string>
   to: string
   transform: (v: any) => unknown
}> = []

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

export function applyPolymorphs<R extends object>(input: R): Promise<R> {
   if (!input) return input

   return POLYMORPHS.reduce(async (value, polymorph) => {
      const keys = Object.values(polymorph.enum)

      const props = await Promise.all(
         Object.entries(await value).map(async ([k, v]) => {
            const r = Array.isArray(v)
               ? await Promise.all(v.map(applyPolymorphs))
               : typeof v === 'object'
               ? await applyPolymorphs(v)
               : v
            return [k, r]
         })
      )

      const [match, noMatch] = partition(props, ([key]) => keys.includes(key))

      const children = await Promise.all(
         match
            .map(([k, v]) => [k, toArray(v)] as [string, object[]])
            .map(([type, providers]) =>
               Promise.all(providers.map(provider => polymorph.transform({ type, ...provider })))
            )
      )

      return { ...Object.fromEntries(noMatch), [polymorph.to]: children.flat() }
   }, Promise.resolve(input))
}

export function registerPolymorph<T extends object>(
   to: string,
   enm: Record<string, string>,
   transform: (v: T) => T | Promise<T> = v => v
) {
   POLYMORPHS.push({ to, enum: enm, transform })
}
