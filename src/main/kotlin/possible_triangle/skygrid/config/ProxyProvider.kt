package possible_triangle.skygrid.config

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.chunk.ChunkAccess
import kotlin.random.Random

@ExperimentalSerializationApi
abstract class ProxyProvider : BlockProvider() {

    abstract fun get(random: Random): BlockProvider

    final override fun base(random: Random, blocks: Registry<Block>): Block {
        return get(random).base(random, blocks)
    }

    override fun generateBase(random: Random, chunk: ChunkAccess, blocks: Registry<Block>, pos: BlockPos) {
        get(random).generate(random, chunk, blocks, pos)
    }

}