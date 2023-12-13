package com.possible_triangle.skygrid.api.xml.elements.providers

import com.possible_triangle.skygrid.api.extensions.keyFrom
import com.possible_triangle.skygrid.api.extensions.random
import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("tag")
data class Tag(
    private val id: String,
    private val mod: String = "minecraft",
    @XmlSerialName("weight", "", "") private val tagWeight: Double = 1.0,
    private val random: Boolean = true,
    private val expand: Boolean = false,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
) : BlockProvider() {

    @Transient
    private lateinit var blocks: List<Block>

    @Transient
    private val location = keyFrom(id, mod)

    @Transient
    private val key = TagKey.create(BuiltInRegistries.BLOCK.key(), location)

    override val weight: Double
        get() = if (expand)
            blocks.size * tagWeight
        else tagWeight

    override val name: String
        get() = "#$location"

    override fun flat(): List<Pair<Block, Double>> = blocks.map { it to 1.0 }

    override fun internalValidate(
        blocks: Registry<Block>,
        references: IReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean {
        this.blocks = blocks.getTagOrEmpty(key).map { it.value() }.filter {
            filters.all { filter -> filter.test(it, blocks) }
        }
        return this.blocks.isNotEmpty()
    }

    override fun base(random: RandomSource): Block {
        return if (this.random) this.blocks.random(random)
        else this.blocks.first()
    }
}