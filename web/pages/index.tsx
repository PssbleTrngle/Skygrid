import type { GetStaticProps, NextPage } from 'next'
import Link from 'next/link'
import Page from '../components/basic/Page'
import { Named } from '../types'
import { getStaticConfigs } from '../util/data/configs'

interface Props {
   configs: Named[]
}

export const getStaticProps: GetStaticProps<Props> = async () => {
   const configs = getStaticConfigs()
   return { props: { configs } }
}

const Home: NextPage<Props> = ({ configs }) => {
   return (
      <Page>
         {configs.map(({ mod, id }) => (
            <Link key={`${mod}-${id}`} href={`${mod}/${id}`} passHref>
               <a>{id}</a>
            </Link>
         ))}
      </Page>
   )
}

export default Home
