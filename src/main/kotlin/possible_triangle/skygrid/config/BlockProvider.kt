package possible_triangle.skygrid.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import possible_triangle.skygrid.config.impl.Side
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
abstract class BlockProvider {

    abstract val weight: Double

    abstract val sides: List<Side>
    abstract val transformers: List<Transformer>

    @Transient
    lateinit var validSides: List<Side>

    protected abstract fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        validSides = sides.filter { it.validate(blocks, tags) }
        return internalValidate(blocks, tags)
    }

    internal abstract fun base(random: Random, blocks: Registry<Block>): Block

    fun get(random: Random, blocks: Registry<Block>): BlockState {
        return transformers.fold(base(random, blocks).defaultBlockState()) { state, transformer ->
            transformer.apply(state, random)
        }
    }

    protected open fun generateBase(random: Random, chunk: ChunkAccess, blocks: Registry<Block>, pos: BlockPos) {
        val state = get(random, blocks)
        chunk.setBlockState(pos, state, false)
    }

    fun generate(random: Random, chunk: ChunkAccess, blocks: Registry<Block>, pos: BlockPos) {
        generateBase(random, chunk, blocks, pos)
        validSides.forEach { it.generate(random, chunk, blocks, pos) }
    }

}