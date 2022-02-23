import type { GetStaticPaths, GetStaticProps, InferGetStaticPropsType, NextPage } from 'next'
import Page from '../../components/basic/Page'
import ConfigVisualizer from '../../components/config/ConfigVisualizer'
import { getConfigs, getStaticConfig } from '../../util/data/configs'

type Props = InferGetStaticPropsType<typeof getStaticProps>

export const getStaticPaths: GetStaticPaths = async () => {
   const configs = getConfigs()
   return { paths: configs.map(params => ({ params })), fallback: false }
}

export const getStaticProps: GetStaticProps = async ({ params }) => {
   const parsed = await getStaticConfig(params)
   return { props: { parsed } }
}

const Config: NextPage<Props> = ({ parsed }) => {
   return (
      <Page>
         <ConfigVisualizer config={parsed} />
      </Page>
   )
}

export default Config
