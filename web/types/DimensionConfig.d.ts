import { TypedProvider } from './BlockProviders'
import WeightedEntry from './WeightedEntry'

export interface LootTable extends WeightedEntry {
   id: string
}

export default interface DimensionConfig {
   loot?: {
      table: LootTable[]
   }
   blocks: {
      children: TypedProvider[]
   }
}
