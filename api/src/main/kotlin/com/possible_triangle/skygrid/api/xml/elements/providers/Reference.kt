package com.possible_triangle.skygrid.api.xml.elements.providers

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block

@Serializable
@SerialName("reference")
class Reference(
    private val id: String,
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
) :
    ProxyProvider() {

    override val name: String
        get() = id

    @Transient
    private lateinit var provider: BlockProvider

    override fun flat(): List<Pair<Block, Double>> = this.provider.flat()

    override fun internalValidate(
        blocks: HolderLookup.RegistryLookup<Block>,
        references: IReferenceContext,
        filters: List<FilterOperator>
    ): Boolean {
        return references[id]?.also {
            provider = it
        } != null
    }

    override fun get(random: RandomSource): BlockProvider {
        return provider
    }
}