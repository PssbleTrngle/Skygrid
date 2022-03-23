import { Polymorph } from 'util/polymorphism'
import { Named } from '.'
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
   icon?: string | null
   except?: {
      mod?: ModFilter[] | ModFilter
      name?: NameFilter[] | NameFilter
      tag?: TagFilter[] | TagFilter
   }
}

export interface Block extends BaseBlockProvider, Named {}

interface Generated {
   occurenced: number
}

export interface GeneratedBlock extends Block, Generated {
   type: ProviderType.BLOCK
}

export interface GeneratedTag extends Tag, Generated {
   type: ProviderType.TAG
}

export interface Tag extends BaseBlockProvider, Named {
   matches: Block[]
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
