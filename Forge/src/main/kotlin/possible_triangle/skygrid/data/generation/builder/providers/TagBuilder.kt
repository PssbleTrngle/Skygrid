package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.generation.builder.ExceptFilterBuilder
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Tag

class TagBuilder(
    private val id: String,
    private val mod: String? = null,
    private val weight: Double = 1.0,
    private val random: Boolean,
    private val expand: Boolean,
) :
    BlockProviderBuilder<Tag>() {

    private val filters = arrayListOf<FilterOperator>()

    fun except(builder: ExceptFilterBuilder.() -> Unit) {
        ExceptFilterBuilder().also {
            builder(it)
            filters.add(it.build())
        }
    }

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>): Tag {
        return Tag(id, mod, weight, random, expand, extras, transformers, filters.toList())
    }

}