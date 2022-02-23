export interface ModFilter {
   id: string
}

export interface NameFilter {
   pattern: string
}

export interface TagFilter {
   id: string
   mod?: string
}
