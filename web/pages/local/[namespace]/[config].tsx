import { NextPage } from 'next'
import { useRouter } from 'next/router'
import Page from '../../../components/basic/Page'
import ConfigVisualizer from '../../../components/config/ConfigVisualizer'
import { useLocalFile } from '../../../hooks/useLocalFiles'
import { ResourceType } from '../../../util/parser/XMLParser'

const LocalConfig: NextPage = () => {
   const { namespace, config } = useRouter().query
   const parsed = useLocalFile(ResourceType.CONFIG, { mod: namespace, id: config })

   return <Page>{parsed && <ConfigVisualizer config={parsed} />}</Page>
}

export default LocalConfig
