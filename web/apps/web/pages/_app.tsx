import type { AppProps } from "next/app";
import getConfig from "next/config";
import NextImage from "next/image";
import NextLink from "next/link";
import { useRouter } from "next/router";
import Footer from "ui/components/Footer";
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

const createImg: ElementsContext["createImg"] = ({ size, src, ...props }) => {
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

function MyApp({ Component, pageProps }: AppProps) {
  const router = useRouter();
  return (
    <ElementsProvider value={elementsContext}>
      <RouterProvider value={router}>
        <ThemeProvider theme={dark}>
          <Global />
          <Component {...pageProps} />
          <Footer />
        </ThemeProvider>
      </RouterProvider>
    </ElementsProvider>
  );
}

export default MyApp;
