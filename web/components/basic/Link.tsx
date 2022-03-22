import { StyledIcon } from '@styled-icons/styled-icon'
import NextLink, { LinkProps as BaseLinkProps } from 'next/link'
import { useRouter } from 'next/router'
import { createElement, FC, HTMLAttributes, useMemo, useState } from 'react'
import styled, { css } from 'styled-components'
import { UnderlineHover } from 'styles/mixins'
import { Underlined } from './Text'

interface LinkProps {
   underline?: 'always' | 'none' | 'hover'
}

export const LinkStyle = css<LinkProps>`
   ${p => (!p.underline || p.underline === 'hover') && UnderlineHover};
   text-decoration: ${p => (p.underline === 'always' ? 'underline' : 'none')};
   color: ${p => p.theme.text};
   cursor: pointer;
`

const Link: FC<
   LinkProps & Omit<BaseLinkProps, 'passHref'> & Omit<HTMLAttributes<HTMLAnchorElement>, 'href'>
> = ({ children, href, locale, prefetch, replace, scroll, shallow, as, ...props }) => {
   const { asPath } = useRouter()
   const nextProps = { href, locale, prefetch, replace, scroll, shallow, as }
   const relativeHref = useMemo(() => {
      if (typeof href === 'string' && href.startsWith('./')) {
         const base = asPath.endsWith('/') ? asPath : `${asPath}/`
         return base + href.slice(2)
      } else {
         return href
      }
   }, [href, asPath])

   if (typeof href === 'string' && href.startsWith('http')) {
      return (
         <Style {...props} href={href}>
            {children}
         </Style>
      )
   }

   return (
      <NextLink {...nextProps} href={relativeHref} passHref>
         <Style {...props}>{children}</Style>
      </NextLink>
   )
}

const Style = styled.a`
   ${LinkStyle}
`

export const IconLink: FC<{
   href: string
   icon: StyledIcon
   hoverIcon?: StyledIcon
   size?: string | number
   tooltip?: string
}> = ({ children, tooltip, icon, hoverIcon = icon, size = '1em', ...props }) => {
   const [hovered, setHover] = useState(false)
   return (
      <ContainerLink
         {...props}
         underline='none'
         title={tooltip}
         onPointerEnter={() => setHover(true)}
         onPointerLeave={() => setHover(false)}>
         {createElement(hovered ? hoverIcon : icon, { size })}
         {children && <Underlined>{children}</Underlined>}
      </ContainerLink>
   )
}

const ContainerLink = styled(Link)`
   display: flex;
   align-items: center;
`

export default Link
