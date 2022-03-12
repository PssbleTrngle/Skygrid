package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
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

    abstract fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean

    abstract fun offset(pos: BlockPos): BlockPos

    override fun validate(
        blocks: Registry<Block>,
        tags: TagContainer,
        references: ReferenceContext,
        additionalFilters: List<FilterOperator>
    ): Boolean {
        validProviders = WeightedList(providers.filter { it.validate(blocks, tags, references, ) })
        return internalValidate(blocks, tags) && validProviders.isNotEmpty()
    }

    override fun generate(random: Random, access: IBlockAccess): Boolean {
        val providerRandom = Random(random.nextLong())

        if (random.nextDouble() > probability) return false
        val at = offset(BlockPos(0, 0, 0))

        return validProviders.random(random).generate(providerRandom) { state, pos ->
            access.set(state, pos.offset(at))
        }
    }

}