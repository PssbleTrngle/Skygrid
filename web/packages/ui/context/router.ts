import { createContext, useContext } from "react";
import { ParsedUrlQuery } from "querystring";
import type { UrlObject } from "url";

type Url = string | UrlObject;

const error = () => {
  throw new Error("router context missing!");
};

const CTX = createContext<RouterContext>({
  query: {},
  push: error,
  replace: error,
});

export interface RouterContext {
  query: ParsedUrlQuery;
  replace(url: Url, as?: Url): unknown;
  push(url: Url, as?: Url): unknown;
}

export function useRouter(): RouterContext {
  return useContext(CTX);
}

export const RouterProvider = CTX.Provider;
