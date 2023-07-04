package com.possible_triangle.skygrid.datagen.builder.providers

import net.minecraft.core.RegistryAccess
import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.Reference

class ReferenceBuilder(
    private val id: String,
    private val weight: Double = 1.0,
    override val registries: RegistryAccess,
) :
    BlockProviderBuilder<Reference>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Reference {
        return Reference(id, weight, extras, transformers, filters)
    }
}