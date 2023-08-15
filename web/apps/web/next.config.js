const transpile = require("next-transpile-modules");

const mcVersion = "1.19";
const basePath = "/" + mcVersion;

module.exports = transpile(["ui", "schema"])({
  reactStrictMode: true,
  basePath,
  publicRuntimeConfig: { basePath, mc_version: mcVersion },
  compiler: {
    styledComponents: true,
  },
});
