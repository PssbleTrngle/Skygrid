import { Options } from 'config'
import {
   createReadStream,
   createWriteStream,
   existsSync,
   readdirSync,
   statSync,
   unlinkSync,
} from 'fs'
import { ensureDirSync } from 'fs-extra'
import klaw from 'klaw-sync'
import { uniq } from 'lodash'
import Merger from 'Merger'
import { dirname, extname, join, relative } from 'path'
import { TagDefinition } from 'types'
import unzipper from 'unzipper'

export default async function extractData(options: Options) {
   const from = join(options.temp, 'sources')
   const out = join(options.temp, 'extracted')

   const assetFolders = [/^assets\/\w+\/(textures|models|blockstates)/]

   const merger = new Merger()

   if (options.data) {
      ensureDirSync(options.data)
      merger.register<Record<string, string>>({
         pattern: /^data\/(\w+)\/skygrid\/([\w\/]+).xml$/,
         merge: (a, b) => ({ ...a, ...b }),
         path: (mod, id) => join(options.data!, mod, 'skygrid', `${id}.xml`),
      })

      merger.register<TagDefinition>({
         pattern: /^data\/(\w+)\/tags\/blocks\/([\w\/]+).json$/,
         merge: (a, b) => (b.replace ? b : { values: uniq([...a.values, ...b.values]) }),
         path: (mod, id) => join(options.data!, mod, 'tags', `${id}.json`),
      })
   }

   if (options.lang) {
      ensureDirSync(options.lang)
      merger.register<Record<string, string>>({
         pattern: /^assets\/\w+\/lang\/(\w+).json$/,
         merge: (a, b) => ({ ...a, ...b }),
         path: lang => join(options.lang!, `${lang}.json`),
      })
   }

   const isCached = existsSync(out)
   if (!options.cached || !isCached) {
      if (isCached) unlinkSync(out)

      const children = readdirSync(from).map(f => join(from, f))

      const jars = children.filter(
         f => (statSync(f).isFile() && extname(f) === '.jar') || extname(f) === '.zip'
      )

      const folders = children.filter(f => statSync(f).isDirectory())

      const consumer: Consumer = async (entry, path) => {
         const dataMatch = await merger.extractData(entry, path)
         if (assetFolders.some(it => it.test(path))) {
            const file = join(out, path)
            const folder = dirname(file)
            ensureDirSync(folder)
            entry.pipe(createWriteStream(file))
            return true
         }
         return dataMatch
      }

      await Promise.all([
         ...jars.map(it => extractJar(it, consumer)),
         ...folders.map(it => handleFolder(it, consumer)),
      ])

      console.log(`Unzipped ${jars.length} jars`)
   } else {
      console.log('Using cached data')
   }
}

type Consumer = (entry: NodeJS.ReadableStream, path: string) => Promise<boolean>

async function handleFolder(folder: string, consumer: Consumer) {
   const entries = klaw(folder, { nodir: true })
   await Promise.all(
      entries.map(e => {
         return consumer(createReadStream(e.path), relative(folder, e.path).replace(/\\/g, '/'))
      })
   )
}

async function extractJar(jar: string, consumer: Consumer) {
   const stream = createReadStream(jar)
   const entries = stream.pipe(unzipper.Parse({ forceStream: true }))

   for await (const entry of entries) {
      if (entry.type !== 'File') continue
      const handled = await consumer(entry, entry.path)
      if (!handled) entry.autodrain()
   }
}
