package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.impl.Fallback
import com.possible_triangle.skygrid.datagen.builder.BasicBlocksBuilder
import com.possible_triangle.skygrid.datagen.builder.IBlocksBuilder
import net.minecraft.core.RegistryAccess

class FallbackBuilder(
    private val name: String?,
    private val weight: Double = 1.0,
    override val registries: RegistryAccess,
) : BlockProviderBuilder<Fallback>(), IBlocksBuilder {

    private val children = BasicBlocksBuilder(registries)

    override fun add(block: BlockProviderBuilder<*>) = children.add(block)

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Fallback {
        return Fallback(name, weight, extras, transformers, filters, children.build())
    }

}