import type { GetServerSideProps, InferGetServerSidePropsType, NextPage } from 'next'
import Page from '../components/basic/Page'
import ConfigVisualizer from '../components/config/ConfigVisualizer'
import { parseFile } from '../util/parser'

type Props = InferGetServerSidePropsType<typeof getServerSideProps>

export const getServerSideProps: GetServerSideProps = async () => {
   const parsed = await parseFile(
      '../src/main/resources/data/minecraft/skygrid/dimensions/overworld.xml'
   )
   return { props: { parsed } }
}

const Home: NextPage<Props> = ({ parsed }) => {
   return (
      <Page>
         <ConfigVisualizer config={parsed} />
      </Page>
   )
}

export default Home
