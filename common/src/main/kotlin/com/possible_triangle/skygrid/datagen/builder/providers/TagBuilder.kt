package com.possible_triangle.skygrid.datagen.builder.providers

import com.possible_triangle.skygrid.api.xml.elements.Extra
import com.possible_triangle.skygrid.api.xml.elements.FilterOperator
import com.possible_triangle.skygrid.api.xml.elements.Transformer
import com.possible_triangle.skygrid.api.xml.elements.providers.Tag
import net.minecraft.core.RegistryAccess

class TagBuilder(
    private val id: String,
    private val mod: String = "minecraft",
    private val weight: Double = 1.0,
    private val random: Boolean,
    private val expand: Boolean,
    override val registries: RegistryAccess,
) :
    BlockProviderBuilder<Tag>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Tag {
        return Tag(id, mod, weight, random, expand, extras, transformers, filters)
    }

}