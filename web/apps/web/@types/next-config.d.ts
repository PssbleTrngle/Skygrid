import 'next/config'

interface Config {
   mcVersion: string
   basePath: string
}

type getConfig = () => {
   publicRuntimeConfig: Config
}

declare module 'next/config' {
   export default getConfig
}
