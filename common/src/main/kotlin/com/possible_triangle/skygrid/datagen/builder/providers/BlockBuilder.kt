package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import com.possible_triangle.skygrid.datagen.DatagenContext

class BlockBuilder(
    override val context: DatagenContext,
    private val id: String,
    private val mod: String = context.defaultMod,
    private val weight: Double = 1.0,
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