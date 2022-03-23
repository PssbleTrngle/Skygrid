import { uniqBy } from 'lodash'
import { Block, BlockProvider, Reference, Tag } from 'util/parser/types/BlockProviders'
import LocalDataResolver from './LocalDataResolver'
import { Named } from './types'
import XMLParser from './XMLParser'

function request<R>(endpoint: string, body: unknown): Promise<R | null> {
   return fetch(`/api/${endpoint}`, {
      method: 'POST',
      body: JSON.stringify(body),
      headers: {
         'Content-Type': 'application/json',
         Accept: 'application/json',
      },
   })
      .then(r => r.json())
      .catch(() => null)
}

export default class LocalDataParser extends XMLParser {
   constructor(handle: FileSystemDirectoryHandle) {
      super(new LocalDataResolver(handle))
   }

   async getBlocksFor(tag: Pick<Tag, 'mod' | 'id' | 'except'>) {
      const [local, server] = await Promise.all([
         super.getBlocksFor(tag),
         request<Block[]>('tag', tag),
      ])
      return uniqBy([...local, ...(server ?? [])], it => `${it.mod}:${it.id}`)
   }

   async getPreset(reference: Reference) {
      const local = await super.getPreset(reference)
      if (local) return local
      return request<BlockProvider>('preset', reference)
   }

   async getIcon({ mod, id }: Named) {
      const response = await request<{ icon: string }>('icon', { mod, id })
      return response?.icon ?? null
   }
}
