import { createContext, HTMLAttributes, useContext } from "react";
import styled from "styled-components";

const CTX = createContext<ElementsContext>({
  createLink: (_, p) => p,
  createImg: ({ src, size, objectFit, ...props }) => (
    <DefaultImg {...props} $size={size} $objectFit={objectFit} src={src} />
  ),
});

const DefaultImg = styled.img<{ $size: number; $objectFit?: string }>`
  object-fit: ${(p) => p.$objectFit};
  height: ${(p) => `${p.$size}px`};
  width: ${(p) => `${p.$size}px`};
`;

type ImgElementStyle = NonNullable<JSX.IntrinsicElements["img"]["style"]>;

type ImgFactoryProps = {
  src: string;
  size: number;
  objectFit?: ImgElementStyle["objectFit"];
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
