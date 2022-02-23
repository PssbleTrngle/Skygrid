import { Polymorph } from '../util/polymorphism'
import { ModFilter, NameFilter, TagFilter } from './Filters'
import WeightedEntry from './WeightedEntry'

export enum ProviderType {
   LIST = 'list',
   FALLBACK = 'fallback',
   REFERENCE = 'reference',
   TAG = 'tag',
   BLOCK = 'block',
}

export interface BlockProviders extends Record<ProviderType, BlockProvider> {
   list: BlockList
   fallback: Fallback
   reference: Reference
   tag: Tag
   block: Block
}

export interface BlockProvider extends WeightedEntry {
   weight: number
   uuid: string
}

export type TypedProvider<Type extends ProviderType = ProviderType> = Polymorph<
   BlockProviders,
   Type
> & {
   uuid: string
}

export interface Block extends BlockProvider {
   id: string
   mod?: string
   name?: string
}

export interface GeneratedBlock extends Block {}

export interface Tag extends BlockProvider {
   id: string
   mod?: string
   matches: Block[]
   except?: {
      mod?: ModFilter[] | ModFilter
      name?: NameFilter[] | NameFilter
      tag?: TagFilter[] | TagFilter
   }
}

export interface Reference extends BlockProvider {
   id: string
   provider: TypedProvider
}

export interface BlockList extends BlockProvider {
   name?: string
   children: TypedProvider[]
}

export interface Fallback extends BlockList {}
