import type { GetServerSideProps, InferGetServerSidePropsType, NextPage } from 'next'
import Page from '../components/basic/Page'
import ConfigVisualizer from '../components/config/ConfigVisualizer'
import { parseFile } from '../util/parser'

type Props = InferGetServerSidePropsType<typeof getServerSideProps>

const PARSED = parseFile('../src/main/resources/data/minecraft/skygrid/dimensions/overworld.xml')

export const getServerSideProps: GetServerSideProps = async ctx => {
   const parsed = await PARSED
   return { props: { parsed } }
}

const Home: NextPage<Props> = ({ parsed }) => {
   return (
      <Page>
         <h1>Home</h1>

         <ConfigVisualizer config={parsed} />
      </Page>
   )
}

export default Home
