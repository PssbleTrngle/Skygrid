import Background from "layout/Background";
import Button from "ui/components/basic/Button";
import Link from "ui/components/basic/Link";
import Page from "layout/Page";
import { Subtitle, Title } from "ui/components/basic/Text";
import LinkBar from "ui/components/LinkBar";
import ResourceLinks from "ui/components/ResourceLinks";
import type { GetStaticProps, NextPage } from "next";
import styled from "styled-components";
import serverParser from "parser/serverParser";
import { Named } from "schema/generated/types";
import { ResourceType } from "parser/XMLParser";
import Panels from "ui/components/Panels";
import Main from "ui/components/Main";

interface Props {
  configs: Named[];
}

export const getStaticProps: GetStaticProps<Props> = async () => {
  const configs = await serverParser.getResources(ResourceType.CONFIG);
  return { props: { configs } };
};

const Home: NextPage<Props> = ({ configs }) => {
  return (
    <>
      <Background />
      <Style>
        <ModTitle>Skygrid</ModTitle>

        <Description>
          <li>
            Skygrid provides a new world type which generates an infinite grid
            of blocks in the void, inspired by{" "}
            <Link
              underline="always"
              href="https://www.youtube.com/watch?v=5dhs3ithXDA"
            >
              Sethblings version
            </Link>{" "}
            from 2012.
          </li>
          <li>
            By default generates in the 3 vanilla dimensions and contains
            support for several mods
          </li>
          <li>
            Is highly configurable using datapacks and can be extended to
            generate in every dimension you want
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
              <Button disabled title="Temporarily disabled">
                Choose directory
              </Button>
            </div>
          </Panels>
        </Main>
      </Style>
    </>
  );
};

const Style = styled(Page)`
  padding: 0;
`;

const ModTitle = styled(Title)`
  text-transform: uppercase;
  //color: #b9d3ff;
  font-size: 8rem;
  padding: 4rem 0;
  margin-bottom: 3rem;
  letter-spacing: 0.5ch;
`;

const Description = styled.ul`
  list-style: disc;
  margin: 0 auto;
  font-size: 1.5rem;
  width: max-content;
  max-width: 1000px;

  li {
    margin: 0.5em 0;
  }
`;

export default Home;
