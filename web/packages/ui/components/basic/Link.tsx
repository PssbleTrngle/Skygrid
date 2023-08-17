import { StyledIcon } from "@styled-icons/styled-icon";
import { Underlined } from "./Text";
import { createElement, FC, HTMLAttributes, ReactNode, useState } from "react";
import styled, { css } from "styled-components";
import { UnderlineHover } from "../../styles/mixins";
import { useElementFactory } from "../../context/elements";

export interface LinkProps {
  underline?: "always" | "none" | "hover";
  children?: ReactNode;
  href?: string;
}

export const LinkStyle = css<LinkProps>`
  ${(p) => (!p.underline || p.underline === "hover") && UnderlineHover};
  text-decoration: ${(p) => (p.underline === "always" ? "underline" : "none")};
  color: ${(p) => p.theme.text};
  cursor: pointer;
`;

const Link: FC<LinkProps & HTMLAttributes<HTMLAnchorElement>> = ({
  children,
  ...props
}) => {
  const { createLink } = useElementFactory();
  const component = <Style {...props}>{children}</Style>;
  if (createLink && props.href) return createLink(props.href, component);
  return component;
};

const Style = styled.a<LinkProps>`
  ${LinkStyle}
`;

export interface IconLinkProps {
  icon: StyledIcon;
  hoverIcon?: StyledIcon;
  size?: string | number;
  tooltip?: string;
  href?: string;
  children?: ReactNode;
}

export const IconLink: FC<IconLinkProps> = ({
  children,
  tooltip,
  icon,
  hoverIcon = icon,
  size = "1em",
  ...props
}) => {
  const [hovered, setHover] = useState(false);
  return (
    <ContainerLink
      {...props}
      underline="none"
      title={tooltip}
      onPointerEnter={() => setHover(true)}
      onPointerLeave={() => setHover(false)}
    >
      {createElement(hovered ? hoverIcon : icon, { size })}
      {children && <Underlined>{children}</Underlined>}
    </ContainerLink>
  );
};

const ContainerLink = styled(Link)`
  display: flex;
  align-items: center;
`;

export default Link;
