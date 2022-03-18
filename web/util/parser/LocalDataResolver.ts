import { Block } from '../../@types/BlockProviders'
import DataResolver from './DataResolver'

type Handle<K extends FileSystemHandleKind> = K extends 'directory'
   ? FileSystemDirectoryHandle
   : FileSystemFileHandle

async function handleAt<K extends FileSystemHandleKind>(
   parent: FileSystemDirectoryHandle,
   type: K,
   path: string[]
): Promise<Handle<K>> {
   const [first, ...rest] = path.join('/').split('/')

   if (!first) {
      if (type === 'directory') return parent as Handle<K>
      else throw Error(`Invalid path: ${path.join('/')}`)
   }

   if (rest.length === 0 && type === 'file') {
      return parent.getFileHandle(first) as Promise<Handle<K>>
   }

   const child = await parent.getDirectoryHandle(first)
   if (first.length === 0) return child as Handle<K>
   return handleAt(child, type, rest)
}

export default class LocalDataResolver implements DataResolver {
   constructor(private readonly directory: FileSystemDirectoryHandle) {}

   async exists(type: FileSystemHandleKind, ...path: string[]) {
      try {
         await handleAt(this.directory, type, path)
         return true
      } catch {
         return false
      }
   }

   async getContent(...path: string[]) {
      if (!(await this.exists('file', ...path))) return null
      const handle = await handleAt(this.directory, 'file', path)
      const file = await handle.getFile()
      return file.text()
   }

   async lastModified(...path: string[]) {
      const handle = await handleAt(this.directory, 'file', path)
      const file = await handle.getFile()
      return file.lastModified
   }

   async list(type: FileSystemHandleKind, ...path: string[]): Promise<string[]> {
      const handle = await handleAt(this.directory, 'directory', path)

      const entries = []
      for await (const [name, entry] of handle.entries()) {
         if (entry.kind === type) entries.push(name)
      }
      return entries
   }

   async getName(block: Block) {
      return null
   }
}
