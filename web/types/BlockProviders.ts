import { Polymorph } from '../util/polymorphism'
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

interface BlockProvider extends WeightedEntry {
   weight: number
   uuid: string
}

export type TypedProvider<Type extends ProviderType = ProviderType> = Polymorph<BlockProviders, Type> & {
   uuid: string
}

export interface Block extends BlockProvider {
   weight: number
   id: string
   mod?: string
}

export interface GeneratedBlock extends Block {}

export interface Tag extends Block {}

export interface Reference extends BlockProvider {
   weight: number
   id: string
}

export interface BlockList extends BlockProvider {
   weight: number
   name?: string
   children: TypedProvider[]
}

export interface Fallback extends BlockList {}
