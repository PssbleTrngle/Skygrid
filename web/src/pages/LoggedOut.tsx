import { VFC } from 'react'
import Page from '../components/Page'

const LoggedOut: VFC = () => (
   <Page>
      <h1>You are logged out</h1>
      <p>
         Use <code>/web</code> in-game to retrieve login link
      </p>
   </Page>
)

export default LoggedOut
