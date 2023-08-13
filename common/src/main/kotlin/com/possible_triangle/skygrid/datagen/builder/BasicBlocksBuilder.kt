package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.elements.BlockProvider
import com.possible_triangle.skygrid.datagen.DatagenContext
import com.possible_triangle.skygrid.datagen.builder.providers.BlockProviderBuilder

class BasicBlocksBuilder(override val context: DatagenContext) : IBlocksBuilder {

    private val children = arrayListOf<BlockProviderBuilder<*>>()

    fun each(builder: BlockProviderBuilder<*>.() -> Unit = {}) {
        children.forEach(builder)
    }

    fun build(additional: BlockProviderBuilder<*>.() -> Unit = {}): List<BlockProvider> {
        return children.map { it.also(additional).build() }
    }

    override fun add(block: BlockProviderBuilder<*>) {
        children.add(block)
    }

}