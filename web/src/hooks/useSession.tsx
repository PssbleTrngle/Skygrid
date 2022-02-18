import { createContext, FC, useContext, useEffect, useReducer } from 'react'
import { useQuery } from 'react-query'
import { useLocation, useNavigate } from 'react-router-dom'
import LoggedOut from '../pages/LoggedOut'
import { delay } from '../util'

export interface Player {
   name: string
   uuid: string
}

export interface Session {
   token: string
   player: Player
}

const CTX = createContext<Session | null>(null)

export default function useSession(): Session {
   const session = useContext(CTX)
   if (session) return session
   throw new Error('Session Provider missing')
}

export const SessionProvider: FC = ({ children }) => {
   const { search, pathname } = useLocation()
   const navigate = useNavigate()

   const [token, setToken] = useReducer((_: string | null, v: string | null) => {
      if (v) localStorage.setItem('token', v)
      else localStorage.removeItem('token')
      return v
   }, localStorage.getItem('token'))

   const { data: player } = useQuery(
      'player',
      delay({ name: 'Example Player', uuid: 'c10aac8c-31cc-47bc-8ed8-b8e63cf5c1cc' }, false)
   )

   useEffect(() => {
      const queryToken = new URLSearchParams(search).get('token')
      if (queryToken && token !== queryToken) {
         setToken(queryToken)
         navigate({ pathname })
      }
   }, [search, token])

   if (!token) return <LoggedOut />
   if (!player) return <p>Loading...</p>
   return <CTX.Provider value={{ token, player }}>{children}</CTX.Provider>
}
