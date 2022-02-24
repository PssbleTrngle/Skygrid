package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Block

class BlockBuilder(private val id: String, private val mod: String? = null) :
    BlockProviderBuilder<Block>() {

    override fun buildWith(weight: Double, extras: List<Extra>, transformers: List<Transformer>): Block {
        return Block(id, mod, weight, extras, transformers)
    }
}