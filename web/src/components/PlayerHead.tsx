import { useMemo, VFC } from 'react'
import { useQuery } from 'react-query'
import { Player } from '../hooks/useSession'

interface PlayerData {
   name: string
   id: string
   properties: Array<{
      name: string
      value: string
   }>
}

async function fetchData(uuid: string): Promise<PlayerData> {
   const url = `https://cors-anywhere.herokuapp.com/https://sessionserver.mojang.com/session/minecraft/profile/${uuid.replaceAll(
      '-',
      ''
   )}`
   const response = await fetch(url)
   if (!response.ok) throw new Error(response.statusText)
   return response.json()
}

const PlayerHead: VFC<Pick<Player, 'uuid'>> = ({ uuid }) => {
   const { data } = useQuery(['skin', uuid], () => fetchData(uuid), { refetchInterval: false })
   const texture = useMemo(() => data?.properties.find(p => p.name === 'textures')?.value, [data])
   return <img src={`data:image/png;base64, ${texture}`} alt={data?.name} />
}

export default PlayerHead
