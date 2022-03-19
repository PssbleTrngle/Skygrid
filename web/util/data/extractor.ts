import {
   createReadStream,
   createWriteStream,
   existsSync,
   mkdirSync,
   readdirSync,
   unlinkSync,
   writeFileSync,
} from 'fs'
import { groupBy, uniq } from 'lodash'
import { dirname, extname, join } from 'path'
import unzipper from 'unzipper'
import { Named } from '../../@types'
import { TagDefinition } from '../parser/XMLParser'
import Renderer from '../renderer'
import { getDataPath, Merger } from './serverParser'

const mods = ['minecraft', 'quark', 'biomesoplenty', 'botania']

const pathOf = ({ mod, id }: Named) => {
   return `temp/public/blocks/${mod}/${id}.png`
}

async function render(dir: string, overwriteExisting = false) {
   const render = new Renderer(dir)

   const blocks = await render.getBlocks()

   console.log(`Found ${blocks.length} missing models for ${dir}`)

   let results: PromiseSettledResult<Named>[] = []
   for (const block of blocks.flat())
      try {
         const path = pathOf(block)
         mkdirSync(dirname(path), { recursive: true })

         const buffer = await render.render(block)

         writeFileSync(path, buffer)

         results.push({ status: 'fulfilled', value: block })
      } catch (e) {
         results.push({ status: 'rejected', reason: e })
      }

   const { fulfilled, rejected } = groupBy(results.flat(), r => r.status)
   if (fulfilled) console.log(`Rendered ${fulfilled.length} blocks`)
   if (rejected) {
      const reasons = groupBy(rejected as PromiseRejectedResult[], r => r.reason)
      console.group(`Failed to render ${rejected.length} blocks`)
      Object.entries(reasons).map(([reason, { length }]) => console.log(`${reason}: ${length}`))
      console.groupEnd()
   }
}

export default async function createRenders(useCached = true) {
   const dir = 'temp'

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

   const assetsDir = join(dir, 'assets')
   const isCached = existsSync(assetsDir)
   if (!useCached || !isCached) {
      if (isCached) unlinkSync(assetsDir)

      const jars = readdirSync(dir)
         .filter(f => extname(f) === '.jar')
         .map(f => join(dir, f))

      console.log(`Found ${jars.length} jars`)

      const assetFolders = [/^assets\/\w+\/(textures|models|blockstates)/]

      await Promise.all(
         jars.map(async jar => {
            const stream = createReadStream(jar)
            const entries = stream.pipe(unzipper.Parse({ forceStream: true }))

            for await (const entry of entries) {
               if (entry.type !== 'File') continue
               await merger.extractData(entry)
               if (assetFolders.some(it => it.test(entry.path))) {
                  const file = join(dir, entry.path)
                  const folder = dirname(file)
                  if (!existsSync(folder)) mkdirSync(folder, { recursive: true })
                  entry.pipe(createWriteStream(file))
               } else {
                  entry.autodrain()
               }
            }
         })
      )

      console.log(`Unzipped ${jars.length} jars`)
   }

   return render(assetsDir)
}
