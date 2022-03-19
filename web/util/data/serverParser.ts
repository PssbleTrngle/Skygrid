import { existsSync, mkdirSync, readdirSync, readFileSync, statSync, writeFileSync } from 'fs'
import { dirname, join, parse, resolve } from 'path'
import { PassThrough } from 'stream'
import { Entry } from 'unzipper'
import { Named } from '../../@types'
import { Block } from '../../@types/BlockProviders'
import DataResolver from '../parser/DataResolver'
import XMLParser from '../parser/XMLParser'

const DATA_DIR = resolve('data')

function isType(file: string, type: FileSystemHandleKind) {
   if (type === 'directory') return statSync(file).isDirectory()
   if (type === 'file') return statSync(file).isFile()
   return false
}

export const serverResolver: DataResolver = {
   exists: async (type, ...path) => {
      const file = resolve(...path)
      if (!existsSync(file)) return false
      return isType(file, type)
   },
   getContent: async (...path) => {
      const file = resolve(...path)
      if (!existsSync(file)) return null
      return readFileSync(file).toString()
   },
   getName: async block => nameOf(block),
   list: async (type, ...path) => {
      const file = resolve(...path)
      if (!existsSync(file)) return []
      return readdirSync(file).filter(it => isType(join(file, it), type))
   },
}

const serverParser = new XMLParser(serverResolver)

function getNamespaces() {
   return readdirSync(DATA_DIR).filter(it => statSync(resolve(DATA_DIR, it)).isDirectory())
}

export function getDataPath({ id, mod }: Named, type: string, extension: string) {
   return resolve(DATA_DIR, mod ?? 'minecraft', type, `${id}.${extension}`)
}

export function getDataEntries(type: string) {
   const namespaces = getNamespaces()
   return namespaces.flatMap(mod => {
      const dir = resolve(DATA_DIR, mod, type)
      if (!existsSync(dir)) return []
      const files = readdirSync(dir).filter(it => statSync(resolve(dir, it)).isFile())
      return files.map(file => ({ id: parse(file).name, mod }))
   })
}

const langFile = join('lang', 'en_us.json')
const translations = existsSync(langFile) ? JSON.parse(readFileSync(langFile).toString()) : {}

export function nameOf(block: Block): string | null {
   return translations[`block.${block.mod ?? 'minecraft'}.${block.id}`] ?? null
}

interface DataMerger<T> {
   pattern: RegExp
   merge(a: T, b: T): T
   path(...args: string[]): string
}

function pipeString(stream: PassThrough) {
   return new Promise<string>((res, rej) => {
      const chunks: Buffer[] = []
      stream.on('data', chunk => chunks.push(Buffer.from(chunk)))
      stream.on('error', rej)
      stream.on('end', () => res(Buffer.concat(chunks).toString('utf8')))
   })
}

export class Merger {
   private mergers: DataMerger<unknown>[] = []

   register<T>(merger: DataMerger<T>) {
      this.mergers.push(merger)
   }

   async extractData(entry: Entry) {
      await Promise.all(
         this.mergers.map(async ({ pattern, path, merge }) => {
            const match = entry.path.match(pattern)
            if (match)
               try {
                  const content = await pipeString(entry)
                  const resource = JSON.parse(content)
                  const out = resolve(path(...match.slice(1)))
                  if (existsSync(out)) {
                     const existing = JSON.parse(readFileSync(out).toString())
                     writeFileSync(out, JSON.stringify(merge(existing, resource), null, 2))
                  } else {
                     mkdirSync(dirname(out), { recursive: true })
                     writeFileSync(out, content)
                  }
               } catch (e) {
                  throw new Error(`Could not proccess ${entry.path}: ${(e as Error).message}`)
               }
         })
      )
   }
}

export default serverParser
