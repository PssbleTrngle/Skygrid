package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.BlockList
import com.possible_triangle.skygrid.datagen.DatagenContext
import com.possible_triangle.skygrid.datagen.builder.BasicBlocksBuilder
import com.possible_triangle.skygrid.datagen.builder.IBlocksBuilder

class BlockListBuilder(
    override val context: DatagenContext,
    private val name: String?,
    private val weight: Double = 1.0,
) : BlockProviderBuilder<BlockList>(), IBlocksBuilder {

    private val children = BasicBlocksBuilder(context)

    override fun add(block: BlockProviderBuilder<*>) = children.add(block)

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): BlockList {
        return BlockList(weight, name, extras, transformers, filters, children.build())
    }

}