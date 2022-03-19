import Link from 'next/link'
import { useRouter } from 'next/router'
import { VFC } from 'react'
import { Named } from '../util/parser/types'

const ResourceLinks: VFC<{ keys: Named[] }> = ({ keys }) => {
   const { pathname } = useRouter()

   return (
      <div>
         {keys.map(({ mod, id }) => (
            <Link key={`${mod}-${id}`} href={`${pathname}/${mod}/${id}`} passHref>
               <a>{id}</a>
            </Link>
         ))}
      </div>
   )
}

export default ResourceLinks
