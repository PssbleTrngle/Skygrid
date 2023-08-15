const mcVersion = '1.18'
const basePath = '/' + mcVersion

/** @type {import('next').NextConfig} */
const nextConfig = {
   basePath,
   reactStrictMode: true,
   compiler: {
      styledComponents: true,
   },
}

module.exports = nextConfig
