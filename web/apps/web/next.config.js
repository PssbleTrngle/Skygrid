const path = require("path");
const withPlugins = require("next-compose-plugins");
const transpile = require("next-transpile-modules");
const analyze = require("@next/bundle-analyzer");

const mcVersion = "1.19";
const basePath = "/" + mcVersion;

module.exports = withPlugins(
  [
    transpile(["ui", "schema"]),
    analyze({ enabled: process.env.ANALYZE === "true" }),
  ],
  {
    reactStrictMode: true,
    output: "standalone",
    experimental: {
      outputFileTracingRoot: path.join(__dirname, "../../"),
    },
    basePath,
    publicRuntimeConfig: { basePath, mc_version: mcVersion },
    compiler: {
      styledComponents: true,
    },
  }
);
