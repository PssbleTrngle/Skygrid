//@ts-ignore
import axios from 'axios'
import { Options } from 'config'
import { createWriteStream, existsSync } from 'fs'
import { copySync, ensureDirSync } from 'fs-extra'
import Curseforge from 'node-curseforge'
import { basename, join } from 'path'
import parseSources, { Source } from 'sources'

function getEnv(key: string) {
   const env = process.env[key]
   if (!env) throw new Error(`Environment Variable '${key}' missing`)
   return env
}

const request = axios.create({ responseType: 'stream' })

function replaceEnv(string: string) {
   const matches = string.match(/(\$\w+)($|\s)/g) ?? []
   return matches
      .map(it => it.slice(1))
      .reduce((s, key) => {
         const env = getEnv(key)
         return s.replace(s, env)
      }, string)
}

async function fetchSource(source: Source, to: string, useCached: boolean): Promise<void> {
   const outJar = join(to, `${source.name}.jar`)

   if (useCached && existsSync(outJar)) {
      console.log(`Using cached ${source.name}`)
      return
   }

   console.log(`Fetching ${source.name}`)

   try {
      if (source.type === 'curseforge') {
         if (!source.project) throw new Error('Project id missing')
         if (!source.file) throw new Error('File id missing')

         const curseforge = new Curseforge(getEnv('CURSEFORGE_TOKEN'))

         const mod = await curseforge.get_mod(source.project)
         const file = await mod.get_file(source.file)
         await file.download(outJar)
      } else if (source.type === 'file') {
         if (!source.path) throw new Error('File path missing')
         if (!existsSync(source.path)) throw new Error('File not found')

         copySync(source.path, join(to, basename(source.path)))
      } else if (source.url) {
         const url = replaceEnv(source.url)

         const { data } = await request.get(url)
         data.pipe(createWriteStream(outJar))
      }
   } catch (e) {
      console.error(`Error fetching ${source.name}`)
      console.error(e)
   }

   console.log(`Successfully fetched ${source.name}`)
}

export default async function fetchSources(options: Options) {
   const sources = parseSources(options)
   const to = join(options.temp, 'sources')
   console.group(`Fetching ${sources.length} sources`)
   ensureDirSync(to)
   await Promise.all(sources.map(it => fetchSource(it, to, options.cached)))
   console.groupEnd()
}
