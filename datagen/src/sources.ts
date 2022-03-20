import { Options } from 'config'
import { existsSync, readFileSync, statSync } from 'fs'
import { extname } from 'path'

export interface Source {
   name: string
   url?: string
   path?: string
   type?: string
   project?: number
   file?: number
}

function parseJson(file: string): Source[] {
   const content = readFileSync(file).toString()
   const json = JSON.parse(content)
   return json
}

function parseFile(file: string): Source[] {
   const extension = extname(file)
   switch (extension) {
      case '.json':
         return parseJson(file)
      case '.jar':
      case '.zip':
         return [{ type: 'file', path: file, name: file }]
      default:
         throw new Error(`Unkown source type with extension '${extension}'`)
   }
}

export default function parseSources({ from }: Options): Source[] {
   if (existsSync(from)) {
      const info = statSync(from)

      if (info.isFile()) {
         return parseFile(from)
      } else {
         throw new Error('Directory sources not yet supported')
      }
   } else {
      throw new Error(`sources at ${from} could not be found`)
   }
}
