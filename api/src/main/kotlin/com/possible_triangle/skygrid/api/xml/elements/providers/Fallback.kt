package com.possible_triangle.skygrid.api.xml.elements.providers

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block

@Serializable
@SerialName("fallback")
data class Fallback(
    override val name: String? = null,
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
    val children: List<BlockProvider>,
) : ProxyProvider() {

    @Transient
    private lateinit var provider: BlockProvider

    override fun flat(): List<Pair<Block, Double>> = this.provider.flat()

    override fun internalValidate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean {
        provider = children.firstOrNull { it.validate(blocks, references, filters) } ?: return false
        return true
    }

    override fun get(random: RandomSource): BlockProvider {
        return provider
    }

}