import { readFileSync } from 'fs'
import { nanoid } from 'nanoid'
import { Parser } from 'xml2js'
import { ProviderType, TypedProvider } from '../types/BlockProviders'
import DimensionConfig from '../types/DimensionConfig'
import { applyPolymorphs, registerPolymorph } from './polymorphism'

const NUMERICS = ['weight', 'probability', 'offset', 'x', 'y', 'z']

function modify<T, R>(ifIn: string[], mod: (v: T) => R) {
   return (value: T, name: string) => (ifIn.includes(name) ? mod(value) : value)
}

registerPolymorph<TypedProvider>('children', ProviderType, p => ({ ...p, weight: p.weight ?? 1, uuid: nanoid(8) }))

export default async function parseConfig(input: string) {
   const parser = new Parser({
      explicitRoot: false,
      mergeAttrs: true,
      explicitArray: false,
      attrValueProcessors: [modify(NUMERICS, Number.parseFloat)],
   })

   const parsed: DimensionConfig = await parser.parseStringPromise(input)

   return applyPolymorphs(parsed)
}

export function parseFile(file: string) {
   return parseConfig(readFileSync(file).toString())
}
