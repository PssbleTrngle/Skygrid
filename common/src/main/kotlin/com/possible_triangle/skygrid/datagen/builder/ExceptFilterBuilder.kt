package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.elements.Filter
import com.possible_triangle.skygrid.api.xml.elements.filters.ExceptFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.ModFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.NameFilter
import com.possible_triangle.skygrid.api.xml.elements.filters.TagFilter
import com.possible_triangle.skygrid.datagen.DatagenContext
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

class ExceptFilterBuilder(private val context: DatagenContext) {

    private val filters = arrayListOf<Filter>()

    fun pattern(pattern: String) {
        filters.add(NameFilter(pattern))
    }

    fun tag(tag: TagKey<Block>) {
        tag(tag.location.path, tag.location.namespace)
    }

    fun tag(id: String, mod: String = context.defaultMod) {
        filters.add(TagFilter(id, mod))
    }

    fun mod(id: String) {
        filters.add(ModFilter(id))
    }

    fun build(): ExceptFilter {
        return ExceptFilter(filters.toList())
    }

}