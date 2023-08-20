import { FC } from "react";
import styled from "styled-components";
import Image from "ui/components/basic/Image";

const Background: FC = () => (
  <Style>
    <Image
      src="/screenshot.png"
      layout="fill"
      objectFit="cover"
      alt="screenshot of the generation"
    />
  </Style>
);

const Style = styled.section`
  top: 0;
  left: 0;
  position: fixed;
  width: 100%;
  height: 100%;
  filter: blur(0.5rem) brightness(0.75);
`;

export default Background;
