import { config } from 'dotenv'
import { join } from 'path'
import extractData from './extractor'
import createBlockIcons from './renderer'
import fetchSources from './sourceFetcher'

config({ path: '.env.local' })

async function run() {
   const tempDir = 'temp/extracted'
   const useCached = false

   await fetchSources('temp/sources', useCached)

   await extractData('temp/sources', tempDir, useCached)
   await createBlockIcons(join(tempDir, 'assets'), 'public/blocks', !useCached)
}

run().catch(e => console.error(e))
