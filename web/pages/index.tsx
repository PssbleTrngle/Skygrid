import Background from 'components/Background'
import Button from 'components/basic/Button'
import Link from 'components/basic/Link'
import Page from 'components/basic/Page'
import { Subtitle, Title } from 'components/basic/Text'
import LinkBar from 'components/LinkBar'
import ResourceLinks from 'components/ResourceLinks'
import useFileSystem from 'hooks/useFileSystem'
import type { GetStaticProps, NextPage } from 'next'
import { lighten } from 'polished'
import styled from 'styled-components'
import { MaterialShadow } from 'styles/mixins'
import serverParser from 'util/parser/serverParser'
import { Named } from 'util/parser/types'
import { ResourceType } from 'util/parser/XMLParser'

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
      <>
         <Background />
         <Style>
            <ModTitle>Skygrid</ModTitle>

            <Description>
               <li>
                  Skygrid provides a new world type which generates an infinite grid of blocks in
                  the void, inspired by{' '}
                  <Link underline='always' href='https://www.youtube.com/watch?v=5dhs3ithXDA'>
                     Sethblings version
                  </Link>{' '}
                  from 2012.
               </li>
               <li>
                  By default generates in the 3 vanilla dimensions and contains support for several
                  mods
               </li>
               <li>
                  Is highly configurable using datapacks and can be extended to generate in every
                  dimension you want
               </li>
               <li>Generates loot in chests & creates mob spawners</li>
            </Description>

            <LinkBar />

            <Main>
               <Subtitle>Visualize a skygrid config</Subtitle>
               <Panels>
                  <div>
                     <Subtitle>Take a look at the default configurations</Subtitle>
                     <ResourceLinks keys={configs} />
                  </div>
                  <div>
                     <Subtitle>Visualize your local config files</Subtitle>
                     <Button onClick={open}>Choose directory</Button>
                  </div>
               </Panels>
            </Main>
         </Style>
      </>
   )
}

const Style = styled(Page)`
   padding: 0;
`

const Main = styled.main`
   padding: 2rem 4rem 8rem;
   background: ${p => p.theme.bg};
   border-top: 0.25rem solid ${p => lighten(0.04, p.theme.bg)};
   box-shadow: -10px 0 40px 0 #000;
`

const ModTitle = styled(Title)`
   text-transform: uppercase;
   //color: #b9d3ff;
   font-size: 8rem;
   padding: 4rem 0;
   margin-bottom: 3rem;
   letter-spacing: 0.5ch;
`

const Description = styled.ul`
   list-style: disc;
   margin: 0 auto;
   font-size: 1.5rem;
   width: max-content;
   max-width: 1000px;

   li {
      margin: 0.5em 0;
   }
`

const Panels = styled.div`
   display: grid;
   justify-content: center;
   margin-top: 4rem;

   gap: 100px;
   grid-auto-rows: 1fr;
   grid-template-columns: repeat(auto-fit, 500px);

   & > div {
      padding: 2em;
      //backdrop-filter: blur(1rem) brightness(0.75);
      transition: background 0.3s ease, box-shadow 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
      display: grid;
      align-items: center;

      border-radius: 1em;
      ${MaterialShadow};

      &:hover {
         background: ${p => lighten(0.005, p.theme.bg)};
      }
   }
`

export default Home
