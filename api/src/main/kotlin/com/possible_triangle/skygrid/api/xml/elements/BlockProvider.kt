package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.extensions.serialType
import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import com.possible_triangle.skygrid.api.world.fork
import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.Validating
import com.possible_triangle.skygrid.api.xml.warnInvalid
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

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
                references: IReferenceContext,
                filters: List<FilterOperator>,
            ): Boolean = parent.internalValidate(blocks, references, filters)

            override fun base(random: RandomSource): Block = parent.base(random)

            override fun generateBase(random: RandomSource, chunk: IBlockAccess): Boolean =
                parent.generateBase(random, chunk)
        }

        stripped.validExtras = emptyList()
        return stripped
    }

    abstract fun flat(): List<Pair<Block, Double>>

    protected abstract fun internalValidate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean

    final override fun validate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        additionalFilters: List<FilterOperator>,
    ): Boolean {
        val referencesWithThis = references.with(this)
        validExtras = extras.filter { it.validate(blocks, referencesWithThis) }
        return internalValidate(blocks, references, additionalFilters + filters).warnInvalid {
            name?.let {
                "block provider '$name' with type '${serialType}' is invalid and will be ignored"
            } ?: run {
                "unnamed block provider with type '${serialType}' is invalid and will be ignored"
            }
        }
    }

    internal abstract fun base(random: RandomSource): Block

    private fun BlockState.applyTransformers(random: RandomSource): BlockState {
        return transformers.fold(this) { state, transformer ->
            transformer.apply(state, random)
        }
    }

    protected open fun generateBase(random: RandomSource, chunk: IBlockAccess): Boolean {
        val state = base(random).defaultBlockState()
        return chunk.set(state)
    }

    final override fun generate(random: RandomSource, access: IBlockAccess): Boolean {
        val sharedSeed = random.nextLong()
        return generateBase(random, access.fork { it.applyTransformers(random) }).apply {
            if (this) validExtras.forEach {
                it.generate(if (it.shared) RandomSource.create(sharedSeed) else random, access)
            }
        }
    }

}