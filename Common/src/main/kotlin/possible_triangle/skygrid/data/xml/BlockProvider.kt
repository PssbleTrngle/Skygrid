package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import possible_triangle.skygrid.SkygridMod.LOGGER
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.Validating
import possible_triangle.skygrid.world.Generator
import possible_triangle.skygrid.world.IBlockAccess
import kotlin.random.Random

@Serializable
abstract class BlockProvider : WeightedEntry(), Generator<IBlockAccess>, Validating {

    protected abstract val extras: List<Extra>
    protected abstract val transformers: List<Transformer>
    protected abstract val filters: List<FilterOperator>
    abstract val name: String?

    @Transient
    lateinit var validExtras: List<Extra>

    /**
     * @return the provider without any extras
     */
    fun stripped(): BlockProvider {
        val parent = this
        val stripped = object : BlockProvider() {
            override val extras: List<Extra> = emptyList()
            override val transformers: List<Transformer> = parent.transformers
            override val filters: List<FilterOperator> = parent.filters
            override val name: String? = parent.name
            override val weight: Double
                get() = parent.weight

            override fun flat(): List<Pair<Block, Double>> = parent.flat()

            override fun internalValidate(
                blocks: Registry<Block>,
                tags: TagContainer,
                references: ReferenceContext,
                filters: List<FilterOperator>,
            ): Boolean = parent.internalValidate(blocks, tags, references, filters)

            override fun base(random: Random): Block = parent.base(random)

            override fun generateBase(random: Random, chunk: IBlockAccess): Boolean = parent.generateBase(random, chunk)
        }

        stripped.validExtras = emptyList()
        return stripped
    }

    abstract fun flat(): List<Pair<Block, Double>>

    protected abstract fun internalValidate(
        blocks: Registry<Block>,
        tags: TagContainer,
        references: ReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean

    final override fun validate(
        blocks: Registry<Block>,
        tags: TagContainer,
        references: ReferenceContext,
        additionalFilters: List<FilterOperator>,
    ): Boolean {
        val referencesWithThis = references.with(this)
        validExtras = extras.filter { it.validate(blocks, tags, referencesWithThis) }
        return internalValidate(blocks, tags, references, additionalFilters + filters).also {
            if (!it) LOGGER.debug("Invalid BlockProvider ${name ?: "(anonymous)"} of type ${javaClass.name}")
        }
    }

    internal abstract fun base(random: Random): Block

    private fun getState(random: Random): BlockState {
        return transformers.fold(base(random).defaultBlockState()) { state, transformer ->
            transformer.apply(state, random)
        }
    }

    protected open fun generateBase(random: Random, chunk: IBlockAccess): Boolean {
        val state = getState(random)
        return chunk.set(state)
    }

    final override fun generate(random: Random, access: IBlockAccess): Boolean {
        val sharedSeed = random.nextLong()
        return generateBase(random, access).apply {
            if (this) validExtras.forEach {
                it.generate(if (it.shared) Random(sharedSeed) else random, access)
            }
        }
    }

}