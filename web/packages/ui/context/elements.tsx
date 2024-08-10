import { createContext, HTMLAttributes, useContext } from "react";
import Image from "../components/Image";

const CTX = createContext<ElementsContext>({
  createLink: (_, p) => p,
  createImg: ({ src, size, objectFit, ...props }) => (
    <Image {...props} $size={size} $objectFit={objectFit} src={src} />
  ),
});

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
