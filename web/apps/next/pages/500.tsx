import CenteredPage from "layout/CenteredPage";
import { Title } from "ui/components/basic/Text";
import { NextPage } from "next";

const ErrorPage: NextPage = () => (
  <CenteredPage>
    <Title>500 - Internal Server Error</Title>
  </CenteredPage>
);

export default ErrorPage;
