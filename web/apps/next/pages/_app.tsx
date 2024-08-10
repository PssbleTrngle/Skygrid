import type { AppProps } from "next/app";
import getConfig from "next/config";
import Head from "next/head";
import NextImage from "next/image";
import NextLink from "next/link";
import { useRouter } from "next/router";
import { Centered } from "ui/components/basic/Text";
import Footer from "ui/components/Footer";
import DefaultImg from "ui/components/Image";
import SourceDisplay, { CIProps } from "ui/components/SourceDisplay";
import { ElementsContext, ElementsProvider } from "ui/context/elements";
import { RouterProvider } from "ui/context/router";
import Global from "ui/styles/global";
import "ui/styles/reset.css";
import ThemeProvider from "ui/styles/theme";
import dark from "ui/styles/theme/dark";

const { publicRuntimeConfig } = getConfig();

const createLink: ElementsContext["createLink"] = (href, link) => (
  <NextLink href={href} passHref legacyBehavior>
    {link}
  </NextLink>
);

const createImg: ElementsContext["createImg"] = ({
  size,
  src,
  objectFit,
  ...props
}) => {
  if (src.startsWith("https://")) {
    return (
      <DefaultImg {...props} $size={size} $objectFit={objectFit} src={src} />
    );
  }

  const realSrc = src.startsWith("/")
    ? publicRuntimeConfig.basePath + src
    : src;

  return (
    <NextImage {...props} src={realSrc} width={size} height={size}></NextImage>
  );
};

const elementsContext: ElementsContext = {
  createLink,
  createImg,
};

/* eslint-disable turbo/no-undeclared-env-vars */
const ciProps: CIProps = {
  repository: process.env.NEXT_PUBLIC_GIT_REPOSITORY ?? "local",
  sha: process.env.NEXT_PUBLIC_GIT_SHA,
  version: process.env.NEXT_PUBLIC_GIT_VERSION ?? "dev",
};
/* eslint-enable turbo/no-undeclared-env-vars */

function MyApp({ Component, pageProps }: AppProps) {
  const router = useRouter();
  return (
    <ElementsProvider value={elementsContext}>
      <RouterProvider value={router}>
        <ThemeProvider theme={dark}>
          <Head>
            <meta
              name="viewport"
              content="width=device-width, initial-scale=0.5"
            />
          </Head>
          <Global />
          <Component {...pageProps} />
          <Footer>
            <Centered>
              <SourceDisplay {...ciProps} />
            </Centered>
          </Footer>
        </ThemeProvider>
      </RouterProvider>
    </ElementsProvider>
  );
}

export default MyApp;
