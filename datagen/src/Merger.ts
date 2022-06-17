import { existsSync, readFileSync, writeFileSync } from 'fs'
import { ensureDirSync } from 'fs-extra'
import { dirname, extname, resolve } from 'path'

interface DataMerger<T> {
   pattern: RegExp
   merge(a: T, b: T): T
   path(...args: string[]): string
}

function pipeString(stream: NodeJS.ReadableStream) {
   return new Promise<string>((res, rej) => {
      const chunks: Buffer[] = []
      stream.on('data', chunk => chunks.push(Buffer.from(chunk)))
      stream.on('error', rej)
      stream.on('end', () => res(Buffer.concat(chunks).toString('utf8')))
   })
}

export default class Merger {
   private mergers: DataMerger<unknown>[] = []

   register<T>(merger: DataMerger<T>) {
      this.mergers.push(merger)
   }

   async extractData(entry: NodeJS.ReadableStream, fileName: string) {
      const matches = await Promise.all(
         this.mergers.map(async ({ pattern, path, merge }) => {
            const match = fileName.match(pattern)
            if (match)
               try {
                  const content = await pipeString(entry)
                  const out = resolve(path(...match.slice(1)))
                  ensureDirSync(dirname(out))

                  if (extname(fileName) === '.json') {
                     try {
                        const resource = JSON.parse(content)
                        if (existsSync(out)) {
                           const existing = JSON.parse(readFileSync(out).toString())
                           writeFileSync(out, JSON.stringify(merge(existing, resource), null, 2))
                        } else {
                           writeFileSync(out, content)
                        }
                     } catch (e) {
                        console.log(`Skipping ${fileName}: ${(e as Error).message}`)
                     }
                  } else {
                     writeFileSync(out, content)
                  }
               } catch (e) {
                  throw new Error(`Could not proccess ${fileName}: ${(e as Error).message}`)
               }

            return !!match
         })
      )

      return matches.some(it => it)
   }
}
