package com.possible_triangle.skygrid.datagen.builder

import net.minecraft.core.RegistryAccess
import com.possible_triangle.skygrid.datagen.builder.providers.BlockProviderBuilder
import com.possible_triangle.skygrid.api.xml.elements.BlockProvider

class BasicBlocksBuilder(override val registries: RegistryAccess) : IBlocksBuilder {

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