import Footer from "ui/components/Footer";
import type { AppProps } from "next/app";
import ThemeProvider from "ui/styles/theme";
import Global from "ui/styles/global";
import "ui/styles/reset.css";
import dark from "ui/styles/theme/dark";
import { RouterProvider } from "ui/context/router";
import { useRouter } from "next/router";

function MyApp({ Component, pageProps }: AppProps) {
  const router = useRouter();
  return (
    <ThemeProvider theme={dark}>
      <RouterProvider value={router}>
        <Global />
        <Component {...pageProps} />
        <Footer />
      </RouterProvider>
    </ThemeProvider>
  );
}

export default MyApp;
