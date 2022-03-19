import { Named } from '.'

export interface ModFilter {
   id: string
}

export interface NameFilter {
   pattern: string
}

export type TagFilter = Named
