package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Tag

class TagBuilder(
    private val id: String,
    private val mod: String = "minecraft",
    private val weight: Double = 1.0,
    private val random: Boolean,
    private val expand: Boolean,
) :
    BlockProviderBuilder<Tag>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Tag {
        return Tag(id, mod, weight, random, expand, extras, transformers, filters)
    }

}