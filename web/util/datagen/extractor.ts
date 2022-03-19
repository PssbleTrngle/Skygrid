import {
   createReadStream,
   createWriteStream,
   existsSync,
   mkdirSync,
   readdirSync,
   unlinkSync,
} from 'fs'
import { uniq } from 'lodash'
import { dirname, extname, join } from 'path'
import unzipper from 'unzipper'
import { getDataPath, Merger } from '../parser/serverParser'
import { TagDefinition } from '../parser/XMLParser'

export default async function extractData(from: string, out: string, useCached = true) {
   const isCached = existsSync(out)
   if (!useCached || !isCached) {
      if (isCached) unlinkSync(out)

      const jars = readdirSync(from)
         .filter(f => extname(f) === '.jar')
         .map(f => join(from, f))

      console.log(`Found ${jars.length} jars`)

      await extractJars(jars, out)

      console.log(`Unzipped ${jars.length} jars`)
   } else {
      console.log('Using cached data')
   }
}

async function extractJars(jars: string[], out: string) {
   const merger = new Merger()
   merger.register<TagDefinition>({
      pattern: /^data\/(\w+)\/tags\/blocks\/([\w\/]+).json$/,
      merge: (a, b) => (b.replace ? b : { values: uniq([...a.values, ...b.values]) }),
      path: (mod, id) => getDataPath({ mod, id }, 'tags', 'json'),
   })

   merger.register<Record<string, string>>({
      pattern: /^assets\/\w+\/lang\/(\w+).json$/,
      merge: (a, b) => ({ ...a, ...b }),
      path: lang => join('lang', `${lang}.json`),
   })

   const assetFolders = [/^assets\/\w+\/(textures|models|blockstates)/]

   await Promise.all(
      jars.map(async jar => {
         const stream = createReadStream(jar)
         const entries = stream.pipe(unzipper.Parse({ forceStream: true }))

         for await (const entry of entries) {
            if (entry.type !== 'File') continue
            await merger.extractData(entry)
            if (assetFolders.some(it => it.test(entry.path))) {
               const file = join(out, entry.path)
               const folder = dirname(file)
               if (!existsSync(folder)) mkdirSync(folder, { recursive: true })
               entry.pipe(createWriteStream(file))
            } else {
               entry.autodrain()
            }
         }
      })
   )
}
