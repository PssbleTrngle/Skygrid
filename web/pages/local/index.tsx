import CenteredPage from 'components/basic/CenteredPage'
import { Subtitle } from 'components/basic/Text'
import ResourceLinks from 'components/ResourceLinks'
import useLocalFiles, { withLocalData } from 'hooks/useLocalFiles'
import { NextPage } from 'next'

const Local: NextPage = withLocalData(() => {
   const { resources } = useLocalFiles()

   return (
      <CenteredPage>
         <Subtitle>Found {resources.dimensions.length} skygrid config files</Subtitle>
         <ResourceLinks keys={resources.dimensions} />
      </CenteredPage>
   )
})

export default Local
