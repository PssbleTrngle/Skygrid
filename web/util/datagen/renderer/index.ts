import { existsSync, mkdirSync, writeFileSync } from 'fs'
import { groupBy } from 'lodash'
import { dirname } from 'path'
import { Named } from '../../parser/types'
import Renderer from './Renderer'

export default async function createBlockIcons(
   from: string,
   to: string,
   overwriteExisting: boolean
) {
   const pathOf = ({ mod, id }: Named) => {
      return `${to}/${mod}/${id}.png`
   }

   const renderer = new Renderer(from)

   const blocks = await renderer.getBlocks()
   const missing = overwriteExisting ? blocks : blocks.filter(it => !existsSync(pathOf(it)))

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
