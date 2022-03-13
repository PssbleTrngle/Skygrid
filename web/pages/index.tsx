import type { GetStaticProps, NextPage } from 'next'
import Link from 'next/link'
import { Named } from '../@types'
import Page from '../components/basic/Page'
import serverParser from '../util/data/serverParser'

interface Props {
   configs: Named[]
}

export const getStaticProps: GetStaticProps<Props> = async () => {
   const configs = await serverParser.getResources('dimensions')
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
