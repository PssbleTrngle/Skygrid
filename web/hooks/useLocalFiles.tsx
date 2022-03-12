import { createContext, FC, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { Named } from '../@types'
import DimensionConfig from '../@types/DimensionConfig'
import Preset from '../@types/Preset'
import parseXML from '../util/parser'

interface ResourceTypes {
   dimensions: DimensionConfig
   presets: Preset
}

type Consumer<T extends keyof ResourceTypes = any> = (value: ResourceTypes[T]) => void

interface Subscriber<T extends keyof ResourceTypes = any> {
   consumer: Consumer<T>
   type: T
   subject: Named
}

interface Context {
   subscribe<T extends keyof ResourceTypes>(
      type: T,
      config: Named,
      consumer: Consumer<T>
   ): () => void
   open(): Promise<void>
   error?: Error
}

const CTX = createContext<Context>({
   open: async () => console.error('Accessing outside of LocalFileProvider'),
   subscribe: () => () => console.error('Accessing outside of LocalFileProvider'),
})

async function read<Kind extends FileSystemHandleKind>(
   handle: FileSystemDirectoryHandle,
   kind: Kind,
   filter: (name: string) => boolean = () => true
) {
   type Result = Kind extends 'file' ? FileSystemFileHandle : FileSystemDirectoryHandle

   const permission = await handle.queryPermission({ mode: 'read' })
   if (permission !== 'granted') await handle.requestPermission({ mode: 'read' })

   const entries = []
   for await (const [name, entry] of handle.entries()) {
      if (entry.kind === kind && filter(name)) entries.push(entry)
   }
   return entries as Result[]
}

export default function useLocalFiles() {
   return useContext(CTX)
}

export function useLocalFile<T extends keyof ResourceTypes>(type: T, subject: Named) {
   const [value, setValue] = useState<ResourceTypes[T]>()
   const { subscribe } = useLocalFiles()
   useEffect(() => subscribe(type, subject, setValue), [type, subject, setValue, subscribe])
   return value
}

async function optionalHandle(parent: FileSystemDirectoryHandle, child: string) {
   try {
      return await parent.getDirectoryHandle(child)
   } catch {
      return null
   }
}

export const LocalFileProvider: FC = ({ children }) => {
   const [directory, openDirectory] = useState<FileSystemDirectoryHandle>()
   const [error, setError] = useState<Error>()
   const [subscribers, setSubscribers] = useState<Subscriber[]>([])
   const lastModifed = useMemo(() => new Map<string, number>(), [directory])

   const loadFiles = useCallback(
      async function <T extends keyof ResourceTypes>(
         skygridHandle: FileSystemDirectoryHandle,
         type: T
      ) {
         const typeHandle = await optionalHandle(skygridHandle, type)
         if (!typeHandle) return []
         const files = await read(typeHandle, 'file')

         return Promise.all(
            files.map(async fileHandle => {
               const file = await fileHandle.getFile()
               const path = await directory?.resolve(fileHandle).then(p => p?.join('/'))
               if (!path) return

               if (lastModifed.get(path) !== file.lastModified) {
                  console.log('reading', path)
                  const content = await file.text()
                  const parsed = await parseXML<ResourceTypes[T]>(content)
                  lastModifed.set(path, file.lastModified)
               }
            })
         )
      },
      [directory, lastModifed]
   )

   const load = useCallback(async () => {
      if (!directory) return
      const namespaces = await read(directory, 'directory')

      await Promise.all(
         namespaces.map(async namespace => {
            const skygridHandle = await optionalHandle(namespace, 'skygrid')
            if (!skygridHandle) return
            await loadFiles(skygridHandle, 'dimensions')
            await loadFiles(skygridHandle, 'presets')
         })
      )
   }, [directory, loadFiles])

   useEffect(() => {
      const interval = setInterval(load, 1000)
      return () => clearInterval(interval)
   }, [load])

   const open = useCallback(async () => {
      setError(undefined)
      try {
         const selected = await window.showDirectoryPicker()
         await selected.queryPermission({ mode: 'read' })
         await load()
         openDirectory(selected)
      } catch (e) {
         console.error(e)
         setError(e as Error)
      }
   }, [])

   const subscribe = useCallback(
      function <T extends keyof ResourceTypes>(type: T, subject: Named, consumer: Consumer<T>) {
         const subscriber = { type, subject, consumer }
         setSubscribers(s => [...s, subscriber])
         return () => setSubscribers(s => s.filter(it => it !== subscriber))
      },
      [setSubscribers]
   )

   return <CTX.Provider value={{ subscribe, open, error }}>{children}</CTX.Provider>
}
