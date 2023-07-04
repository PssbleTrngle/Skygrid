package com.possible_triangle.skygrid.api.xml.elements.impl

import com.possible_triangle.skygrid.api.extensions.keyFrom
import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.world.level.block.Block
import kotlin.random.Random

@Serializable
@SerialName("block")
data class SingleBlock(
    private val id: String,
    private val mod: String = "minecraft",
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
) : BlockProvider() {

    @Transient
    lateinit var block: Block

    @Transient
    private val key = keyFrom(id, mod)

    override val name: String
        get() = key.toString()

    override fun base(random: Random): Block {
        return block
    }

    override fun flat(): List<Pair<Block, Double>> {
        return listOf(block to 1.0)
    }

    override fun internalValidate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean {
        return blocks.getOptional(key).map {
            block = it
        }.isPresent
    }
}