import { VFC } from 'react'
import styled from 'styled-components'
import useSession, { Player } from '../hooks/useSession'
import PlayerHead from './PlayerHead'

const PlayerRow: VFC<Player> = ({ name, uuid }) => {
   const { player } = useSession()
   return (
      <Style>
         <PlayerHead uuid={uuid} />
      </Style>
   )
}

const Style = styled.div``

export default PlayerRow
