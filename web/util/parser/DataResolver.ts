import { Named } from '../../@types'
import { Block, Reference, Tag } from '../../@types/BlockProviders'
import DimensionConfig from '../../@types/DimensionConfig'
import Preset from '../../@types/Preset'

export default interface DataResolver {
   getPreset(reference: Reference): Promise<Preset>
   getConfig(config: Named): Promise<DimensionConfig>
   getBlocksFor(tag: Tag): Promise<Block[]>
}
