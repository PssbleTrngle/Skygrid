import type { GetStaticProps, NextPage } from 'next'
import { Named } from '../@types'
import Page from '../components/basic/Page'
import ResourceLinks from '../components/ResourceLinks'
import serverParser from '../util/data/serverParser'
import { ResourceType } from '../util/parser/XMLParser'

interface Props {
   configs: Named[]
}

export const getStaticProps: GetStaticProps<Props> = async () => {
   const configs = await serverParser.getResources(ResourceType.CONFIG)
   return { props: { configs } }
}

const Home: NextPage<Props> = ({ configs }) => {
   return (
      <Page>
         <ResourceLinks keys={configs} />
      </Page>
   )
}

export default Home
