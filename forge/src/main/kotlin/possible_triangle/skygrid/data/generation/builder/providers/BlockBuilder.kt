package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.SingleBlock

class BlockBuilder(private val id: String, private val mod: String = "minecraft", private val weight: Double = 1.0) :
    BlockProviderBuilder<SingleBlock>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): SingleBlock {
        return SingleBlock(id, mod, weight, extras, transformers, emptyList())
    }
}