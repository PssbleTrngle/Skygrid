package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.util.WeightedList
import possible_triangle.skygrid.world.IBlockAccess
import kotlin.random.Random

@Serializable
abstract class Extra {

    abstract val providers: List<BlockProvider>
    abstract val probability: Double
    abstract val shared: Boolean

    @Transient
    private lateinit var validProviders: WeightedList<BlockProvider>

    abstract fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean

    abstract fun offset(pos: BlockPos): BlockPos

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        validProviders = WeightedList(providers.filter { it.validate(blocks, tags) })
        return internalValidate(blocks, tags) && validProviders.isNotEmpty()
    }

    fun generate(random: Random, chunk: IBlockAccess) {
        if (random.nextDouble() > probability) return
        val at = offset(BlockPos(0, 0, 0))
        validProviders.random(random).generate(random) { state, pos ->
            chunk.set(state, pos.offset(at))
        }
    }

}