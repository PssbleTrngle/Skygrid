import getOptions from 'config'
import extractData from './extractor'
import createBlockIcons from './renderer'
import fetchSources from './sourceFetcher'

async function run() {
   const options = await getOptions()

   await fetchSources(options)
   await extractData(options)
   await createBlockIcons(options)
}

run().catch(e => console.error(e))
