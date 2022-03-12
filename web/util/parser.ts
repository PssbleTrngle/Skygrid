import { readFileSync } from 'fs'

export function parseFile<T extends object>(file: string) {
   return parseXML<T>(readFileSync(file).toString())
}
