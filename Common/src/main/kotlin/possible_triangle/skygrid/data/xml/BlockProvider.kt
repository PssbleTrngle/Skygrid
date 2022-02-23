package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.Constants.LOGGER
import possible_triangle.skygrid.world.IBlockAccess
import kotlin.random.Random

@Serializable
abstract class BlockProvider : WeightedEntry() {

    abstract val extras: List<Extra>
    abstract val transformers: List<Transformer>
    abstract val name: String?

    @Transient
    lateinit var validSides: List<Extra>

    protected abstract fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        validSides = extras.filter { it.validate(blocks, tags) }
        return internalValidate(blocks, tags).also {
            if (!it) LOGGER.debug("Invalid BlockProvider ${name ?: "(anonymous)"} of type ${javaClass.name}")
        }
    }

    internal abstract fun base(random: Random): Block

    private fun getState(random: Random): BlockState {
        return transformers.fold(base(random).defaultBlockState()) { state, transformer ->
            transformer.apply(state, random)
        }
    }

    protected open fun generateBase(random: Random, chunk: IBlockAccess) {
        val state = getState(random)
        if(state.isAir) LOGGER.info("Air detected: $name")
        chunk.set(state)
    }

    fun generate(random: Random, chunk: IBlockAccess) {
        generateBase(random, chunk)
        validSides.forEach { it.generate(random, chunk) }
    }

}