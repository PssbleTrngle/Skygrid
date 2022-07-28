package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.Validating
import possible_triangle.skygrid.util.WeightedList
import possible_triangle.skygrid.world.Generator
import possible_triangle.skygrid.world.IBlockAccess
import kotlin.random.Random

@Serializable
abstract class Extra : Generator<IBlockAccess>, Validating {

    protected abstract val providers: List<BlockProvider>
    abstract val probability: Double
    abstract val shared: Boolean

    @Transient
    lateinit var validProviders: WeightedList<BlockProvider>

    abstract fun internalValidate(blocks: Registry<Block>): Boolean

    abstract fun offset(pos: BlockPos, random: Random): BlockPos

    override fun validate(
        blocks: Registry<Block>,
        references: ReferenceContext,
        additionalFilters: List<FilterOperator>,
    ): Boolean {
        validProviders = WeightedList(providers.filter { it.validate(blocks, references) })
        return internalValidate(blocks) && validProviders.isNotEmpty()
    }

    protected open fun transform(state: BlockState, random: Random): BlockState {
        return state
    }

    override fun generate(random: Random, access: IBlockAccess): Boolean {
        val providerRandom = Random(random.nextLong())
        val offsetRandom = random.nextLong()

        if (random.nextDouble() > probability) return false
        val at = offset(BlockPos(0, 0, 0), Random(offsetRandom))

        return validProviders.random(random).generate(providerRandom) { state, pos ->
            val transformed = transform(state, Random(offsetRandom))
            access.set(state, pos.offset(at))
        }
    }

}