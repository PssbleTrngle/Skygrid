import { get, set } from 'idb-keyval'
import { useRouter } from 'next/router'
import {
   createContext,
   DispatchWithoutAction,
   FC,
   useCallback,
   useContext,
   useEffect,
   useState,
} from 'react'

export enum Status {
   LOADING = 'loading',
   PERMISSION_MISSING = 'permission_missing',
   SELECTED = 'selected',
   NOT_SELECTED = 'not_selected',
}

const CTX = createContext<{
   directory?: FileSystemDirectoryHandle
   setDirectory: (file: FileSystemDirectoryHandle) => Promise<void>
   status: Status
   open: () => Promise<void>
   updatePermission: DispatchWithoutAction
}>({
   status: Status.LOADING,
   updatePermission: () => console.error('Accessing outside of LocalFileProvider'),
   setDirectory: async () => console.error('Accessing outside of LocalFileProvider'),
   open: async () => console.error('Accessing outside of LocalFileProvider'),
})

export default function useFileSystem() {
   return useContext(CTX)
}

export const FileSystemProvider: FC = ({ children }) => {
   const [status, setStatus] = useState<Status>(Status.LOADING)
   const [directory, setHandle] = useState<FileSystemDirectoryHandle>()
   const router = useRouter()

   const setDirectory = useCallback(
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

   useEffect(() => {
      if (!directory) {
         get('directory').then(cached => {
            if (cached) return setDirectory(cached)
            else setStatus(Status.NOT_SELECTED)
         })
      } else {
         updatePermission()
      }
   }, [directory, setDirectory, setStatus, updatePermission])

   const open = useCallback(async () => {
      try {
         const selected = await window.showDirectoryPicker()
         await setDirectory(selected)
         router.push('/local')
      } catch (e) {
         console.error(e)
      }
   }, [router, setDirectory])

   return (
      <CTX.Provider value={{ status, directory, setDirectory, updatePermission, open }}>
         {children}
      </CTX.Provider>
   )
}
