package com.possible_triangle.skygrid.datagen.builder

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import com.possible_triangle.skygrid.api.xml.elements.Filter
import com.possible_triangle.skygrid.api.xml.elements.impl.ExceptFilter
import com.possible_triangle.skygrid.api.xml.elements.impl.ModFilter
import com.possible_triangle.skygrid.api.xml.elements.impl.NameFilter
import com.possible_triangle.skygrid.api.xml.elements.impl.TagFilter

class ExceptFilterBuilder {

    private val filters = arrayListOf<Filter>()

    fun pattern(pattern: String) {
        filters.add(NameFilter(pattern))
    }

    fun tag(tag: TagKey<Block>) {
        tag(tag.location.path, tag.location.namespace)
    }

    fun tag(id: String, mod: String = "minecraft") {
        filters.add(TagFilter(id, mod))
    }

    fun mod(id: String) {
        filters.add(ModFilter(id))
    }

    fun build(): ExceptFilter {
        return ExceptFilter(filters.toList())
    }

}