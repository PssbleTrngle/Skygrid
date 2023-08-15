import NextLink, { LinkProps as NextLinkProps } from "next/link";
import Link, { LinkProps } from "ui/components/basic/Link";
import { FC } from "react";

const StyledLink: FC<Omit<LinkProps, "href"> & Pick<NextLinkProps, "href">> = ({
  children,
  href,
  ...props
}) => {
  return (
    <NextLink href={href} passHref legacyBehavior>
      <Link {...props}>{children}</Link>
    </NextLink>
  );
};

export default StyledLink;
