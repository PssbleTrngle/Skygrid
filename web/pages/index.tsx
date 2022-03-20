import Button from 'components/basic/Button'
import CenteredPage from 'components/basic/CenteredPage'
import { Subtitle, Title } from 'components/basic/Text'
import useFileSystem from 'hooks/useFileSystem'
import type { GetStaticProps, NextPage } from 'next'
import styled from 'styled-components'
import serverParser from 'util/parser/serverParser'
import { Named } from 'util/parser/types'
import { ResourceType } from 'util/parser/XMLParser'
import ResourceLinks from '../components/ResourceLinks'

interface Props {
   configs: Named[]
}

export const getStaticProps: GetStaticProps<Props> = async () => {
   const configs = await serverParser.getResources(ResourceType.CONFIG)
   return { props: { configs } }
}

const Home: NextPage<Props> = ({ configs }) => {
   const { open } = useFileSystem()

   return (
      <CenteredPage>
         <Title>Skygrid</Title>

         <Panels>
            <div>
               <Subtitle>Take a look at the default configurations</Subtitle>
               <ResourceLinks keys={configs} />
            </div>
            <div>
               <Subtitle>Browse you local files</Subtitle>
               <Button onClick={open}>Choose directory</Button>
            </div>
         </Panels>
      </CenteredPage>
   )
}

const Panels = styled.div`
   display: grid;
   justify-content: center;

   gap: 100px;
   grid-auto-rows: 1fr;
   grid-template-columns: repeat(auto-fit, 500px);
   width: 100vw;
`

export default Home
