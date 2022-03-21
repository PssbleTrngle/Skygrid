import Page from 'components/basic/Page'
import ConfigVisualizer from 'components/config/ConfigVisualizer'
import type { GetStaticPaths, GetStaticProps, NextPage } from 'next'
import serverParser from 'util/parser/serverParser'
import { Named } from 'util/parser/types'
import DimensionConfig from 'util/parser/types/DimensionConfig'
import { ResourceType } from 'util/parser/XMLParser'

interface Props {
   configs: Named[]
   parsed: DimensionConfig
}

export const getStaticPaths: GetStaticPaths = async () => {
   const configs = await serverParser.getResources(ResourceType.CONFIG)
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

   const parsed = await serverParser.getConfig({ id: config, mod: namespace })
   if (!parsed) return { notFound: true }

   const configs = await serverParser.getResources(ResourceType.CONFIG)

   return { props: { parsed, configs } }
}

const Config: NextPage<Props> = ({ parsed, configs }) => {
   return (
      <Page>
         <ConfigVisualizer config={parsed} options={configs} />
      </Page>
   )
}

export default Config
