import { get, set } from 'idb-keyval'
import { isEqualWith } from 'lodash'
import { NextPage } from 'next'
import {
   createContext,
   DispatchWithoutAction,
   FC,
   useCallback,
   useContext,
   useEffect,
   useMemo,
   useState,
} from 'react'
import { Named } from '../@types'
import Page from '../components/basic/Page'
import LocalDataParser from '../util/parser/LocalDataParser'
import { Resource, ResourceType } from '../util/parser/XMLParser'

type Consumer<T extends ResourceType = any> = (value: Resource<T>) => void

enum Status {
   LOADING = 'loading',
   PERMISSION_MISSING = 'permission_missing',
   SELECTED = 'selected',
   NOT_SELECTED = 'not_selected',
}

interface Context {
   subscribe<T extends ResourceType>(type: T, config: Named, consumer: Consumer<T>): () => void
   open(): Promise<void>
   error?: Error
   status: Status
   resources: Record<ResourceType, Named[]>
}

const NO_RESOURCES = {
   dimensions: [],
   presets: [],
}

const CTX = createContext<Context>({
   open: async () => console.error('Accessing outside of LocalFileProvider'),
   subscribe: () => () => console.error('Accessing outside of LocalFileProvider'),
   status: Status.LOADING,
   resources: NO_RESOURCES,
})

export default function useLocalFiles() {
   return useContext(CTX)
}

export function useLocalFile<T extends ResourceType>(type: T, subject: Named) {
   const [value, setValue] = useState<Resource<T>>()
   const { subscribe } = useLocalFiles()
   useEffect(() => subscribe(type, subject, setValue), [type, subject, setValue, subscribe])
   return value
}

const keyOf = (s: Named, type: string) => `${type}/${s.mod}:${s.id}`

export const LocalFileProvider: FC = ({ children }) => {
   const [status, setStatus] = useState<Status>(Status.LOADING)
   const [directory, setHandle] = useState<FileSystemDirectoryHandle>()

   const openDirectory = useCallback(
      async (selected: FileSystemDirectoryHandle) => {
         await set('directory', selected)
         setHandle(selected)
      },
      [setHandle]
   )

   const updatePermission = useCallback(() => {
      return directory?.queryPermission({ mode: 'read' }).then(permission => {
         if (permission === 'granted') setStatus(Status.SELECTED)
         else setStatus(Status.PERMISSION_MISSING)
      })
   }, [directory, setStatus])

   const requestPermission = useCallback(async () => {
      await directory?.requestPermission({ mode: 'read' })
      await updatePermission()
   }, [directory, updatePermission])

   const [error, setError] = useState<Error>()

   const subscribers = useMemo(() => new Map<string, Set<Consumer>>(), [])
   const values = useMemo(() => new Map<string, unknown>(), [directory])
   const lastModifed = useMemo(() => new Map<string, number>(), [directory])
   const [resources, setResources] = useState<Record<ResourceType, Named[]>>(NO_RESOURCES)

   useEffect(() => {
      if (!directory) {
         get('directory').then(cached => {
            if (cached) return openDirectory(cached)
            else setStatus(Status.NOT_SELECTED)
         })
      } else {
         updatePermission()
      }
   }, [directory, openDirectory, setStatus, updatePermission])

   const parser = useMemo(() => directory && new LocalDataParser(directory), [directory])

   const load = useCallback(async () => {
      if (!parser || status !== Status.SELECTED) return

      const types = Object.values(ResourceType)

      const resources = await Promise.all(
         types.map(async type => {
            const keys = await parser.getResources(type)
            const subjects = keys.map(it => ({ ...it, key: keyOf(it, type) }))

            const modified = subjects.filter(it => {
               if (!it.lastModified) return true
               if (!lastModifed.has(it.key)) return true
               return it.lastModified > lastModifed.get(it.key)!
            })

            await Promise.all(
               modified.map(async subject => {
                  const parsed = await parser.getConfig(subject)
                  values.set(subject.key, parsed)
                  subscribers.get(subject.key)?.forEach(consume => consume(parsed))
               })
            )

            return { type, keys }
         })
      )

      setResources(current => {
         const same = resources.every(({ type, keys }) => {
            if (keys.length !== current[type].length) return false
            return isEqualWith(current[type], keys, (a, b) => keyOf(a, type) === keyOf(b, type))
         })

         if (same) return current
         return resources.reduce(
            (o, { type, keys }) => ({ ...o, [type]: keys }),
            {} as typeof current
         )
      })
   }, [lastModifed, parser, subscribers, values, status])

   useEffect(() => {
      load()
      const interval = setInterval(load, 1000)
      return () => clearInterval(interval)
   }, [load])

   const open = useCallback(async () => {
      setError(undefined)
      try {
         const selected = await window.showDirectoryPicker()
         await openDirectory(selected)
      } catch (e) {
         console.error(e)
         setError(e as Error)
      }
   }, [openDirectory])

   const subscribe = useCallback(
      function <T extends ResourceType>(type: T, subject: Named, consumer: Consumer<T>) {
         const key = keyOf(subject, type)
         if (values.has(key)) consumer(values.get(key) as T)
         if (!subscribers.has(key)) subscribers.set(key, new Set<Consumer>())
         subscribers.get(key)?.add(consumer)
         return () => subscribers.get(key)?.delete(consumer)
      },
      [subscribers, values]
   )

   if (status === Status.LOADING) return <p>...</p>
   if (status === Status.NOT_SELECTED) return <SelectFolder onOpen={open} />
   if (status === Status.PERMISSION_MISSING)
      return <GrantPermission onRequest={requestPermission} onOpen={open} />

   if (status === Status.SELECTED)
      return (
         <CTX.Provider value={{ subscribe, open, error, resources, status }}>
            {children}
         </CTX.Provider>
      )

   throw new Error('Unkown Status')
}

const GrantPermission: NextPage<{
   onRequest: DispatchWithoutAction
   onOpen: DispatchWithoutAction
}> = ({ onRequest, onOpen }) => (
   <Page>
      <button onClick={onRequest}>Give permission</button>
      <button onClick={onOpen}>Select a different folder</button>
   </Page>
)

const SelectFolder: NextPage<{
   onOpen: DispatchWithoutAction
}> = ({ onOpen }) => (
   <Page>
      <button onClick={onOpen}>
         Select <code>datapack</code> Folder
      </button>
   </Page>
)
