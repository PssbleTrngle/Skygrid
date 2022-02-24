package possible_triangle.skygrid.data.generation.builder

import net.minecraft.tags.Tag
import possible_triangle.skygrid.data.xml.Filter
import possible_triangle.skygrid.data.xml.impl.*

class ExceptFilterBuilder {

    private val filters = arrayListOf<Filter>()

    fun pattern(pattern: String) {
        filters.add(NameFilter(pattern))
    }

    fun tag(tag: Tag.Named<Block>) {
        tag(tag.name.namespace, tag.name.path)
    }

    fun tag(id: String, mod: String? = null) {
        filters.add(TagFilter(id, mod))
    }

    fun mod(id: String) {
        filters.add(ModFilter(id))
    }

    fun build(): ExceptFilter {
        return ExceptFilter(filters.toList())
    }

}