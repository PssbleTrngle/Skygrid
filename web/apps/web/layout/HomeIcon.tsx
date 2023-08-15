import Link from "ui/components/basic/Link";
import Image from "next/image";
import { FC } from "react";
import styled from "styled-components";

const HomeLink: FC = () => (
  <Container underline="none" href="/">
    <Backdrop>
      <Image
        src="/screenshot.png"
        alt="Link to the Homepage"
        layout="fill"
        objectFit="cover"
      />
    </Backdrop>
    <Icon />
  </Container>
);

const Icon = () => (
  <svg style={{ height: "100%", width: "100%" }}>
    <rect x="0%" width="35%" y="0%" height="35%" fill="white" />
    <rect x="65%" width="35%" y="0%" height="35%" fill="white" />
    <rect x="0%" width="35%" y="65%" height="35%" fill="white" />
    <rect x="65%" width="35%" y="65%" height="35%" fill="white" />
  </svg>
);

const Backdrop = styled.div`
  position: relative;
  width: 1500%;
  height: 1500%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  overflow: hidden;

  transition: clip-path 0.2s ease;
  filter: blur(0.2rem);
  clip-path: circle(0%);
`;

const Container = styled(Link)`
  position: relative;
  height: 1.5em;
  width: 1.5em;

  & > * {
    position: absolute;
  }

  &:hover {
    ${Backdrop} {
      clip-path: circle(50%);
    }
  }
`;

export default HomeLink;
