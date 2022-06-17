import Button, { ButtonLink } from 'components/basic/Button'
import CenteredPage from 'components/basic/CenteredPage'
import { Subtitle } from 'components/basic/Text'
import { isEqualWith } from 'lodash'
import {
   createContext,
   createElement,
   DispatchWithoutAction,
   FC,
   useCallback,
   useContext,
   useEffect,
   useMemo,
   useState,
   VFC,
} from 'react'
import LocalDataParser from 'util/parser/LocalDataParser'
import { Named } from 'util/parser/types'
import { Resource, ResourceType } from 'util/parser/XMLParser'
import useFileSystem, { Status } from './useFileSystem'

type Consumer<T extends ResourceType = any> = (value: Resource<T>) => void

const NO_RESOURCES = {
   dimensions: [],
   presets: [],
}

const keyOf = (s: Named, type: string) => `${type}/${s.mod}:${s.id}`

const CTX = createContext<{
   subscribe<T extends ResourceType>(type: T, config: Named, consumer: Consumer<T>): () => void
   error?: Error
   status: Status
   resources: Record<ResourceType, Named[]>
}>({
   subscribe: () => () => console.error('LocalFileEnsurer'),
   resources: NO_RESOURCES,
   status: Status.LOADING,
})

export function withLocalData<P>(component: VFC<P>): VFC<P> {
   const fc: VFC<P> = props => (
      <LocalDataEnsurer>{createElement(component, props)}</LocalDataEnsurer>
   )
   fc.displayName = component.displayName
   return fc
}

const LocalDataEnsurer: FC = ({ children }) => {
   const { status, directory, updatePermission, open } = useFileSystem()

   const requestPermission = useCallback(async () => {
      await directory?.requestPermission({ mode: 'read' })
      await updatePermission()
   }, [directory, updatePermission])

   const [error, setError] = useState<Error>()

   const subscribers = useMemo(() => new Map<string, Set<Consumer>>(), [])
   const values = useMemo(() => new Map<string, unknown>(), [directory])
   const lastModifed = useMemo(() => new Map<string, number>(), [directory])
   const [resources, setResources] = useState<Record<ResourceType, Named[]>>(NO_RESOURCES)

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

   const subscribe = useCallback(
      function <T extends ResourceType>(type: T, subject: Named, consumer: Consumer<T>) {
         const key = keyOf(subject, type)
         if (values.has(key)) consumer(values.get(key) as Resource<T>)
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
      return <CTX.Provider value={{ resources, status, subscribe, error }}>{children}</CTX.Provider>

   throw new Error(`Unknown Status '${status}'`)
}

export function useLocalFile<T extends ResourceType>(type: T, subject: Named) {
   const [value, setValue] = useState<Resource<T>>()
   const { subscribe } = useLocalFiles()
   useEffect(() => subscribe(type, subject, setValue), [type, subject, setValue, subscribe])
   return value
}

export default function useLocalFiles() {
   return useContext(CTX)
}

const GrantPermission: FC<{
   onRequest: DispatchWithoutAction
   onOpen: DispatchWithoutAction
}> = ({ onRequest, onOpen }) => (
   <CenteredPage>
      <Subtitle>Grant permission to access your last directory</Subtitle>
      <Button onClick={onRequest}>Give permission</Button>
      <ButtonLink onClick={onOpen}>Select a different folder</ButtonLink>
   </CenteredPage>
)

const SelectFolder: FC<{
   onOpen: DispatchWithoutAction
}> = ({ onOpen }) => (
   <CenteredPage>
      <button onClick={onOpen}>
         Select <code>datapack</code> Folder
      </button>
   </CenteredPage>
)
