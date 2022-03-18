import { NextPage } from 'next'
import Page from '../../components/basic/Page'
import ResourceLinks from '../../components/ResourceLinks'
import useLocalFiles from '../../hooks/useLocalFiles'

const Local: NextPage = () => {
   const { resources } = useLocalFiles()

   return (
      <Page>
         <ResourceLinks keys={resources.dimensions} />
      </Page>
   )
}

export default Local
