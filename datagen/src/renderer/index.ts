import { Options } from 'config'
import { existsSync, mkdirSync, writeFileSync } from 'fs'
import { groupBy } from 'lodash'
import { dirname, join } from 'path'
import { Named } from 'types'
import Renderer from './Renderer'

export default async function createBlockIcons(options: Options) {
   if (!options.icons) return

   const from = join(options.temp, 'extracted', 'assets')

   const pathOf = ({ mod, id }: Named) => {
      return `${options.icons}/${mod}/${id}.png`
   }

   const renderer = new Renderer(from)

   const blocks = await renderer.getBlocks()
   const missing = options.cached ? blocks.filter(it => !existsSync(pathOf(it))) : blocks

   console.log(`Found ${missing.length} missing models for ${from}`)

   let results: PromiseSettledResult<Named>[] = []
   for (const block of missing.flat())
      try {
         const path = pathOf(block)

         mkdirSync(dirname(path), { recursive: true })

         const buffer = await renderer.render(block)

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
      Object.entries(reasons).forEach(([reason, { length }]) => console.log(`${reason}: ${length}`))
      console.groupEnd()
   }
}
