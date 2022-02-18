package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.chunk.ChunkAccess
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.util.WeightedList
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
@SerialName("side")
data class Side(
    val on: String,
    val providers: List<BlockProvider>,
    val offset: Int = 1,
    val probability: Double = 1.0,
    val shared: Boolean = false,
) {

    @Transient
    lateinit var validProviders: WeightedList<BlockProvider>

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        validProviders = WeightedList(providers.filter { it.validate(blocks, tags) }.associateWith { it.weight })
        return Direction.byName(on) != null && offset > 0 && validProviders.isNotEmpty()
    }

    fun generate(random: Random, chunk: ChunkAccess, blocks: Registry<Block>, pos: BlockPos) {
        if (random.nextDouble() > probability) return
        val state = validProviders.random(random).get(random, blocks)
        val direction = Direction.byName(on) ?: throw NullPointerException("Unknown direction '$on'")
        chunk.setBlockState(pos.relative(direction, offset), state, false)
    }

}