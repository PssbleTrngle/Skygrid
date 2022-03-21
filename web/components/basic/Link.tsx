import NextLink, { LinkProps } from 'next/link'
import { useRouter } from 'next/router'
import { FC, useMemo } from 'react'
import styled from 'styled-components'

const Link: FC<Omit<LinkProps, 'passHref'>> = ({
   children,
   href,
   locale,
   prefetch,
   replace,
   scroll,
   shallow,
   ...props
}) => {
   const { asPath } = useRouter()
   const nextProps = { href, locale, prefetch, replace, scroll, shallow }
   const relativeHref = useMemo(() => {
      if (typeof href === 'string' && href.startsWith('./')) {
         const base = asPath.endsWith('/') ? asPath : `${asPath}/`
         return base + href.slice(2)
      } else {
         return href
      }
   }, [href, asPath])

   return (
      <NextLink {...nextProps} href={relativeHref} passHref>
         <a {...props}>{children}</a>
      </NextLink>
   )
}

export const InvisibleLink = styled(Link)`
   text-decoration: none;
   color: ${p => p.theme.text};
`

export default Link
