import CenteredPage from 'components/basic/CenteredPage'
import Link from 'components/basic/Link'
import { Centered, Title } from 'components/basic/Text'
import { NextPage } from 'next'

const NotFound: NextPage = () => (
   <CenteredPage>
      <Title>404 - Not Found</Title>
      <Centered>
         <Link href='/'>take me home</Link>
      </Centered>
   </CenteredPage>
)

export default NotFound
