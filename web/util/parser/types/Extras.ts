import { Polymorph } from 'util/polymorphism'
import { BlockProvider } from './BlockProviders'

export enum ExtrasType {
   SIDE = 'side',
   OFFSET = 'offset',
   CARDINAL = 'cardinal',
}

interface BaseExtra {
   probability: number
   children: BlockProvider[]
}

enum Direction {
   UP = 'up',
   DOWN = 'down',
   NORTH = 'north',
   SOUTH = 'south',
   EAST = 'east',
   WEST = 'west',
}

interface SideExtra extends BaseExtra {
   offset?: number
   on?: Direction
}

interface OffsetExtra extends BaseExtra {
   x?: number
   y?: number
   z?: number
}

interface CardinalExtra extends BaseExtra {
   offset?: number
}

export interface Extras extends Record<ExtrasType, BaseExtra> {
   side: SideExtra
   cardinal: CardinalExtra
   offset: OffsetExtra
}

export type Extra<T extends ExtrasType = ExtrasType> = Polymorph<Extras, T>
