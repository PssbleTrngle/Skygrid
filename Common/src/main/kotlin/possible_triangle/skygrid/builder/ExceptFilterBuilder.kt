package possible_triangle.skygrid.builder

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.Filter
import possible_triangle.skygrid.data.xml.impl.ExceptFilter
import possible_triangle.skygrid.data.xml.impl.ModFilter
import possible_triangle.skygrid.data.xml.impl.NameFilter
import possible_triangle.skygrid.data.xml.impl.TagFilter

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