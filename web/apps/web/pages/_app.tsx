import Footer from "ui/components/Footer";
import type { AppProps } from "next/app";
import ThemeProvider from "ui/styles/theme";
import Global from "ui/styles/global";
import "ui/styles/reset.css";
import dark from "ui/styles/theme/dark";

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <ThemeProvider theme={dark}>
      <Global />
      <Component {...pageProps} />
      <Footer />
    </ThemeProvider>
  );
}

export default MyApp;
