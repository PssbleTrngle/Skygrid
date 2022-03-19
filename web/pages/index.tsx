import type { GetStaticProps, NextPage } from 'next'
import serverParser from 'util/parser/serverParser'
import Page from '../components/basic/Page'
import ResourceLinks from '../components/ResourceLinks'
import { Named } from '../util/parser/types'
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
