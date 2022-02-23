package possible_triangle.skygrid.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.world.BlockAccess
import kotlin.random.Random

@Serializable
abstract class BlockProvider : WeightedEntry() {

    abstract val sides: List<Extra>
    abstract val transformers: List<Transformer>
    abstract val name: String?

    @Transient
    lateinit var validSides: List<Extra>

    protected abstract fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        validSides = sides.filter { it.validate(blocks, tags) }
        return internalValidate(blocks, tags).also {
            if (!it) SkygridMod.LOGGER.debug("Invalid BlockProvider ${name ?: "(anonymous)"} of type ${javaClass.name}")
        }
    }

    internal abstract fun base(random: Random): Block

    fun getState(random: Random): BlockState {
        return transformers.fold(base(random).defaultBlockState()) { state, transformer ->
            transformer.apply(state, random)
        }
    }

    protected open fun generateBase(random: Random, chunk: BlockAccess) {
        val state = getState(random)
        if(state.isAir) SkygridMod.LOGGER.info("Air detected: $name")
        chunk.set(state)
    }

    fun generate(random: Random, chunk: BlockAccess) {
        generateBase(random, chunk)
        validSides.forEach { it.generate(random, chunk) }
    }

}