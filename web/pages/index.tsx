import type { GetStaticProps, InferGetStaticPropsType, NextPage } from 'next'
import Link from 'next/link'
import Page from '../components/basic/Page'
import { getConfigs } from '../util/data/configs'

type Props = InferGetStaticPropsType<typeof getStaticProps>

export const getStaticProps: GetStaticProps = async () => {
   const configs = getConfigs()
   return { props: { configs } }
}

const Home: NextPage<Props> = ({ configs }) => {
   return (
      <Page>
         {configs.map(({ namespace, config }) => (
            <Link key={`${namespace}-${config}`} href={`${namespace}/${config}`} passHref>
               <a>{config}</a>
            </Link>
         ))}
      </Page>
   )
}

export default Home
