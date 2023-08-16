import { createContext, HTMLAttributes, useContext } from "react";
import styled from "styled-components";

const CTX = createContext<ElementsContext>({
  createLink: (_, p) => p,
  createImg: ({ src, size, objectFit, ...props }) => (
    <DefaultImg {...props} $size={size} $objectFit={objectFit} src={src} />
  ),
});

const DefaultImg = styled.img<{ $size?: number; $objectFit?: string }>`
  ${(p) => p.$objectFit && `object-fit: ${p.$objectFit}`};
  ${(p) => p.$size && `height: ${p.$size}px`};
  ${(p) => p.$size && `width: ${p.$size}px`};
`;

type ImgElementStyle = NonNullable<JSX.IntrinsicElements["img"]["style"]>;

export type ImgFactoryProps = {
  src: string;
  size?: number;
  objectFit?: ImgElementStyle["objectFit"];
  layout?: "fill" | "fixed";
  alt?: string;
} & Omit<HTMLAttributes<HTMLImageElement>, "src" | "size" | "placeholder">;

export interface ElementsContext {
  createLink: (href: string, from: JSX.Element) => JSX.Element;
  createImg: (props: ImgFactoryProps) => JSX.Element;
}

export function useElementFactory(): ElementsContext {
  return useContext(CTX);
}

export const ElementsProvider = CTX.Provider;
