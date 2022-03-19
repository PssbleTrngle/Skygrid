//@ts-ignore
import axios from 'axios'
import { createWriteStream, existsSync, mkdirSync } from 'fs'
import Curseforge from 'node-curseforge'
import { join } from 'path'
import sources from './sources.json'

interface Source {
   name: string
   url?: string
   type?: string
   project?: number
   file?: number
}

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
         return s.replaceAll(s, env)
      }, string)
}

async function fetchSource(source: Source, to: string, useCached: boolean): Promise<void> {
   const out = join(to, `${source.name}.jar`)

   if (useCached && existsSync(out)) {
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
         await file.download(out)

         //const url = `https://www.curseforge.com/minecraft/mc-mods/${project}/download/${file}`
         //return fetchSource({ url, name: project }, to)
      } else if (source.url) {
         const url = replaceEnv(source.url)

         const { data } = await request.get(url)
         data.pipe(createWriteStream(out))
      }
   } catch (e) {
      console.error(`Error fetching ${source.name}`)
      console.error(e)
   }

   console.log(`Successfully fetched ${source.name}`)
}

export default async function fetchSources(to: string, useCached: boolean) {
   console.group(`Fetching ${sources.length} sources`)
   if (!existsSync(to)) mkdirSync(to, { recursive: true })
   await Promise.all(sources.map(it => fetchSource(it, to, useCached)))
   console.groupEnd()
}
