import { Github } from "@styled-icons/boxicons-logos/Github";
import { Curseforge } from "@styled-icons/simple-icons/Curseforge";
import Link, { IconLink } from "./basic/Link";
import { Centered } from "./basic/Text";
import { darken } from "polished";
import { FC } from "react";
import styled, { ThemeProvider } from "styled-components";
import dark from "../styles/theme/dark";

export const FOOTER_HEIGHT = "4rem";

// TODO remove this ThemeProvider

const Footer: FC = () => (
  <ThemeProvider theme={dark}>
    <Style>
      <FooterLinks area-label="Footer Navigation">
        <IconLink
          icon={Github}
          tooltip="Github Repository"
          href="https://github.com/PssbleTrngle/Skygrid"
        />
        <IconLink
          icon={Curseforge}
          tooltip="Curseforge Page"
          href="https://www.curseforge.com/minecraft/mc-mods/skygrid"
        />
        <Link
          title="Modrinth Page"
          underline="none"
          href="https://modrinth.com/mod/skygrid"
        >
          <ImageIcon src="https://raw.githubusercontent.com/modrinth/knossos/master/assets/images/logo.svg" />
        </Link>
      </FooterLinks>
      <Centered>© all rights reserved</Centered>
    </Style>
  </ThemeProvider>
);

const ImageIcon = styled.img`
  height: 100%;
  width: 1em;
  padding: 0.1em;
  filter: invert();
`;

const FooterLinks = styled.nav`
  font-size: 2rem;
  display: flex;
  gap: 0.5em;
`;

const Style = styled.footer`
  position: relative;

  background: ${(p) => darken(0.08, p.theme.bg)};
  border-top: 0.25rem solid ${(p) => darken(0.04, p.theme.bg)};

  height: ${FOOTER_HEIGHT};
  display: grid;
  align-items: center;
  grid-template-columns: repeat(3, 1fr);
  padding: 0 1em;
`;

export default Footer;