package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.Reference
import com.possible_triangle.skygrid.datagen.DatagenContext

class ReferenceBuilder(
    override val context: DatagenContext,
    private val id: String,
    private val weight: Double = 1.0,
) :
    BlockProviderBuilder<Reference>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Reference {
        return Reference(id, weight, extras, transformers, filters)
    }
}