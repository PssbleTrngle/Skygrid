import CenteredPage from "layout/CenteredPage";
import { Centered, Title } from "ui/components/basic/Text";
import { NextPage } from "next";
import StyledLink from "../layout/StyledLink";

const NotFound: NextPage = () => (
  <CenteredPage>
    <Title>404 - Not Found</Title>
    <Centered>
      <StyledLink href="/">take me home</StyledLink>
    </Centered>
  </CenteredPage>
);

export default NotFound;
