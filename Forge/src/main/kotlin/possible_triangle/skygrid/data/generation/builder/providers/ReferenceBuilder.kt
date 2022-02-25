package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Reference

class ReferenceBuilder(
    private val id: String,
    private val weight: Double = 1.0,
) :
    BlockProviderBuilder<Reference>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>): Reference {
        return Reference(id, weight, extras, transformers)
    }
}