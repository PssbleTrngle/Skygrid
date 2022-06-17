const basePath = process.env.NEXTJS_BASE_PATH || undefined

/** @type {import('next').NextConfig} */
const nextConfig = {
   basePath,
   reactStrictMode: true,
   compiler: {
      styledComponents: true,
   },
}

module.exports = nextConfig
