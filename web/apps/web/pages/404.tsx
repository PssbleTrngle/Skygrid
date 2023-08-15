import CenteredPage from "layout/CenteredPage";
import Link from "ui/components/basic/Link";
import { Centered, Title } from "ui/components/basic/Text";
import { NextPage } from "next";

const NotFound: NextPage = () => (
  <CenteredPage>
    <Title>404 - Not Found</Title>
    <Centered>
      <Link href="/">take me home</Link>
    </Centered>
  </CenteredPage>
);

export default NotFound;
