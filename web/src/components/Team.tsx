import { VFC } from 'react'
import { useQuery } from 'react-query'
import styled from 'styled-components'
import useSession, { Player } from '../hooks/useSession'
import { delay } from '../util'
import PlayerRow from './PlayerRow'

const Team: VFC = () => {
   const { player } = useSession()
   const { data } = useQuery<Player[]>(
      'team',
      delay([
         {
            name: 'Other Player',
            uuid: 'ec70bcaf-702f-4bb8-b48d-276fa52a780c',
         },
         player,
      ])
   )

   return (
      <Style>
         {data?.map(p => (
            <PlayerRow key={p.uuid} {...p} />
         ))}
      </Style>
   )
}

const Style = styled.section`
   display: grid;
   gap: 1rem;
`

export default Team
