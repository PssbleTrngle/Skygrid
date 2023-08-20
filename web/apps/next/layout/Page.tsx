import { FOOTER_HEIGHT } from "ui/components/Footer";
import Head from "next/head";
import { FC, ReactNode } from "react";
import styled from "styled-components";

const Page: FC<{ children: ReactNode }> = ({ children, ...props }) => (
  <Style {...props}>
    <Head>
      <title>Skygrid</title>
      <meta name="description" content="Analyze skygrid configurations" />
      <link rel="icon" href="/favicon.ico" />
    </Head>
    {children}
  </Style>
);

const Style = styled.section`
  position: relative;
  padding: 2rem;
  min-height: calc(100vh - ${FOOTER_HEIGHT});
  min-height: calc(100dvh - ${FOOTER_HEIGHT});
`;

export default Page;
