import { VFC } from 'react'
import Page from '../components/Page'
import Rewards from '../components/Rewards'
import Team from '../components/Team'
import useSession from '../hooks/useSession'

const Home: VFC = () => {
   const { player } = useSession()
   return (
      <Page>
         <h1>Hello {player.name}</h1>
         <Team />
         <Rewards />
      </Page>
   )
}

export default Home
