import { basename, dirname, join } from 'path'
import DataResolver from 'util/parser/DataResolver'
import { Block } from 'util/parser/types/BlockProviders'
import XMLParser from 'util/parser/XMLParser'

class FakeFileSystem implements DataResolver {
   private files = new Map<string, string>()
   private dirs = new Set<string>()

   put(path: string[], content: string) {
      const file = join(...path)
      path.forEach((_, i) => {
         const parent = path.slice(0, -i - 1)
         if (parent.length === 0) return
         this.dirs.add(join(...parent))
      })
      this.files.set(file, content)
   }

   async exists(type: FileSystemHandleKind, ...path: string[]) {
      const file = join(...path)
      if (type === 'file') return this.files.has(file)
      return this.dirs.has(file)
   }

   async getContent(...path: string[]) {
      const dir = join(...path)
      return this.files.get(dir) ?? null
   }

   async list(type: FileSystemHandleKind, ...path: string[]) {
      const dir = join(...path)
      const keys = Array.from(type === 'file' ? this.files.keys() : this.dirs.keys())
      return keys.filter(it => dirname(it) === dir).map(it => basename(it))
   }

   async getName(_block: Block) {
      return null
   }
}

export default class TestingXMLParser extends XMLParser {
   readonly files: FakeFileSystem

   constructor() {
      const files = new FakeFileSystem()
      super(files)
      this.files = files
   }
}
