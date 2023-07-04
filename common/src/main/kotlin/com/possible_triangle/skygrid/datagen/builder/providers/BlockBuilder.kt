package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import net.minecraft.core.RegistryAccess

class BlockBuilder(
    private val id: String, private val mod: String = "minecraft", private val weight: Double = 1.0,
    override val registries: RegistryAccess,
) :
    BlockProviderBuilder<SingleBlock>() {

    override fun buildWith(
        extras: List<Extra>,
        transformers: List<Transformer>,
        filters: List<FilterOperator>,
    ): SingleBlock {
        return SingleBlock(id, mod, weight, extras, transformers, emptyList())
    }
}