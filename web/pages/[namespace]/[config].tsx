import type { GetStaticPaths, GetStaticProps, NextPage } from 'next'
import Page from '../../components/basic/Page'
import ConfigVisualizer from '../../components/config/ConfigVisualizer'
import DimensionConfig from '../../types/DimensionConfig'
import { getStaticConfig, getStaticConfigs } from '../../util/data/configs'

interface Props {
   parsed: DimensionConfig
}

export const getStaticPaths: GetStaticPaths = async () => {
   const configs = getStaticConfigs()
   return {
      paths: configs.map(({ mod, id }) => ({
         params: {
            namespace: mod,
            config: id,
         },
      })),
      fallback: false,
   }
}

export const getStaticProps: GetStaticProps<Props> = async ({ params }) => {
   const { namespace, config } = params ?? {}
   if (typeof namespace !== 'string') return { notFound: true }
   if (typeof config !== 'string') return { notFound: true }
   const parsed = await getStaticConfig({ id: config, mod: namespace })
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
