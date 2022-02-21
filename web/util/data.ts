import { existsSync, mkdirSync, readFileSync, writeFileSync } from 'fs'
import { nanoid } from 'nanoid'
import { dirname, join, resolve } from 'path'
import { PassThrough } from 'stream'
import { Entry } from 'unzipper'
import { exists } from '.'
import { Block, Tag } from '../types/BlockProviders'

export interface TagDefinition {
   replace?: boolean
   values: string[]
}

const langFile = join('lang', 'en_us.json')
const translations = JSON.parse(readFileSync(langFile).toString())

export function nameOf(block: Block): string | null {
   return translations[`block.${block.mod ?? 'minecraft'}.${block.id}`] ?? null
}

export function blocksForTag(tag: Pick<Tag, 'id' | 'mod'>): Block[] {
   const file = join('data', 'tags', tag.mod ?? 'minecraft', `${tag.id}.json`)
   if (!existsSync(file)) return []

   try {
      const json: TagDefinition = JSON.parse(readFileSync(file).toString())
      return json.values
         .flatMap(value => {
            // TODO default minecraft
            const [mod, id] = value.split(':')
            if (value.startsWith('#')) return blocksForTag({ mod: mod.substring(1), id })
            else return [{ id, mod, weight: 1, uuid: nanoid(8) }]
         })
         .filter(exists)
   } catch {
      return []
   }
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
                     console.log('Merged', out)
                  } else {
                     console.log('Creating', out)
                     mkdirSync(dirname(out), { recursive: true })
                     writeFileSync(out, content)
                  }
               } catch (e) {
                  throw new Error(`Could not proccess ${entry.path}: ${e.message}`)
               }
         })
      )
   }
}
