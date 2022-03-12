import { NextPage } from 'next'
import DimensionConfig from '../../../@types/DimensionConfig'
import Page from '../../../components/basic/Page'
import ConfigVisualizer from '../../../components/config/ConfigVisualizer'

interface Props {
   parsed: DimensionConfig
}

const LocalConfig: NextPage<Props> = ({ parsed }) => {
   return (
      <Page>
         <ConfigVisualizer config={parsed} />
      </Page>
   )
}

export default LocalConfig
