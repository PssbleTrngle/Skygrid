import archiver from 'archiver'
import {
   createReadStream,
   createWriteStream,
   existsSync,
   mkdirSync,
   readdirSync,
   unlinkSync,
   writeFileSync
} from 'fs'
import { groupBy, sumBy, uniq } from 'lodash'
import { BlockModel, Minecraft, RendererOptions } from 'minecraft-render'
import { dirname, extname, join } from 'path'
import unzipper from 'unzipper'
import { Merger, TagDefinition } from './data'

const mods = ['minecraft', 'quark', 'biomesoplenty', 'botania']

const pathOf = ({ blockName }: BlockModel) => {
   if (!blockName) throw new Error('missing blockName')
   const [namespace, id] = blockName.split(':')
   return `public/blocks/${namespace}/${id}.png`
}

async function render(archive: string, overwriteExisting = false) {
   const minecraft = Minecraft.open(archive)
   const blocks = await Promise.all(
      mods.map(async namespace => {
         const names = await minecraft.getBlockNameList(namespace)
         const blocks = await Promise.allSettled(names.map(it => minecraft.getModel(it)))
         return blocks
            .filter(it => it.status === 'fulfilled')
            .map(it => (it as PromiseFulfilledResult<BlockModel>).value)
            .filter(b => overwriteExisting || !existsSync(pathOf(b)))
      })
   )

   const total = sumBy(blocks, b => b.length)
   console.group(`Found ${total} missing models for ${archive}`)
   blocks.forEach(({ length }, i) => console.log(`Found ${length} models for ${mods[i]}`))
   console.groupEnd()

   const options: RendererOptions = { distance: 12 }

   await minecraft.prepareRenderEnvironment(options)
   let results: PromiseSettledResult<BlockModel>[] = []
   for (const block of blocks.flat())
      try {
         const path = pathOf(block)
         mkdirSync(dirname(path), { recursive: true })

         const { buffer, skip } = await minecraft.renderSingle(block)
         if (!buffer) throw new Error(skip)

         writeFileSync(path, buffer)
         results.push({ status: 'fulfilled', value: block })
      } catch (e) {
         results.push({ status: 'rejected', reason: e })
      }

   await minecraft.cleanupRenderEnvironment()

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
   const out = join(dir, 'combined.zip')

   const merger = new Merger()
   merger.register<TagDefinition>({
      pattern: /^data\/(\w+)\/tags\/blocks\/([\w\/]+).json$/,
      merge: (a, b) => (b.replace ? b : { values: uniq([...a.values, ...b.values]) }),
      path: (mod, id) => join('data', 'tags', mod, `${id}.json`),
   })

   merger.register<Record<string, string>>({
      pattern: /^assets\/\w+\/lang\/(\w+).json$/,
      merge: (a, b) => ({ ...a, ...b }),
      path: lang => join('lang', `${lang}.json`),
   })

   const isCached = existsSync(out)
   if (!useCached || !isCached) {
      if (isCached) unlinkSync(out)

      const jars = readdirSync(dir)
         .filter(f => extname(f) === '.jar')
         .map(f => join(dir, f))

      console.log(`Found ${jars.length} jars`)

      const assetFolders = [/^assets\/\w+\/(textures|models|blockstates)/]

      const archive = archiver('zip')
      const combined = createWriteStream(out)
      archive.pipe(combined)

      await Promise.all(
         jars.map(async jar => {
            const stream = createReadStream(jar)
            const entries = stream.pipe(unzipper.Parse({ forceStream: true }))

            for await (const entry of entries) {
               await merger.extractData(entry)
               if (assetFolders.some(it => it.test(entry.path))) {
                  archive.append(entry, { name: entry.path })
               } else {
                  entry.autodrain()
               }
            }
         })
      )

      console.log(`Unzipped ${jars.length} jars`)

      //folders.forEach(folder => archive.directory(join(out, folder), folder))

      await archive.finalize()

      console.log('Combined jars')
   }

   return render(out)
}
