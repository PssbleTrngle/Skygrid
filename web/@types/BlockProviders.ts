import { Named } from '.'
import { Polymorph } from '../util/polymorphism'
import { Extra } from './Extras'
import { ModFilter, NameFilter, TagFilter } from './Filters'
import WeightedEntry from './WeightedEntry'

export enum ProviderType {
   LIST = 'list',
   FALLBACK = 'fallback',
   REFERENCE = 'reference',
   TAG = 'tag',
   BLOCK = 'block',
}

export interface BlockProviders extends Record<ProviderType, BaseBlockProvider> {
   list: BlockList
   fallback: Fallback
   reference: Reference
   tag: Tag
   block: Block
}

interface BaseBlockProvider extends WeightedEntry {
   name?: string | null
   weight: number
   uuid: string
   extras?: Extra[]
   extra?: boolean
}

export interface Block extends BaseBlockProvider, Named {
   icon?: string | null
}

export interface GeneratedBlock extends Block {
   extra: boolean
   occurenced: number
   type: ProviderType.BLOCK
}

export interface Tag extends BaseBlockProvider, Named {
   matches: Block[]
   except?: {
      mod?: ModFilter[] | ModFilter
      name?: NameFilter[] | NameFilter
      tag?: TagFilter[] | TagFilter
   }
}

export interface Reference extends BaseBlockProvider {
   id: string
   provider?: BlockProvider
}

export interface BlockList extends BaseBlockProvider {
   children: BlockProvider[]
}

export interface Fallback extends BlockList {}

export type BlockProvider<T extends ProviderType = ProviderType> = Polymorph<BlockProviders, T>
