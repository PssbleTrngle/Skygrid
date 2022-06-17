const mcVersion = '1.18'
const basePath = '/' + mcVersion

/** @type {import('next').NextConfig} */
const nextConfig = {
   basePath,
   reactStrictMode: true,
   publicRuntimeConfig: { basePath, mc_version: mcVersion },
   compiler: {
      styledComponents: true,
   },
}

module.exports = nextConfig
