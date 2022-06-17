const basePath = process.env.NEXTJS_BASE_PATH || undefined
if (basePath) console.log('Building with basePath', basePath)

/** @type {import('next').NextConfig} */
const nextConfig = {
   basePath,
   reactStrictMode: true,
   compiler: {
      styledComponents: true,
   },
}

module.exports = nextConfig
