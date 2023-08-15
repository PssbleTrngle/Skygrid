import { NextPage } from 'next'
import { useRouter } from 'next/router'
import Page from '../../../components/basic/Page'
import ConfigVisualizer from '../../../components/config/ConfigVisualizer'
import useLocalFiles, { useLocalFile, withLocalData } from '../../../hooks/useLocalFiles'
import { ResourceType } from '../../../util/parser/XMLParser'

const LocalConfig: NextPage = withLocalData(() => {
   const { namespace, config } = useRouter().query
   const { resources } = useLocalFiles()
   const parsed = useLocalFile(ResourceType.CONFIG, {
      mod: namespace as string,
      id: config as string,
   })

   return (
      <Page>{parsed && <ConfigVisualizer options={resources.dimensions} config={parsed} />}</Page>
   )
})

export default LocalConfig
