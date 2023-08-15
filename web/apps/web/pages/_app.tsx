import Footer from "ui/components/Footer";
import type { AppProps } from "next/app";
import ThemeProvider from "ui/styles/theme";
import Global from "ui/styles/global";
import "ui/styles/reset.css";
import dark from "ui/styles/theme/dark";
import { RouterProvider } from "ui/context/router";
import { useRouter } from "next/router";
import NextLink from "next/link";
import NextImage from "next/image";
import { ElementsContext, ElementsProvider } from "ui/context/elements";

const createLink: ElementsContext["createLink"] = (href, link) => (
  <NextLink href={href} passHref legacyBehavior>
    {link}
  </NextLink>
);

const createImg: ElementsContext["createImg"] = ({ size, ...props }) => (
  <NextImage {...props} width={size} height={size}></NextImage>
);

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
