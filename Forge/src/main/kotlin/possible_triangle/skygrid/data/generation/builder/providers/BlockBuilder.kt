package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Block

class BlockBuilder(private val id: String, private val mod: String = "minecraft", private val weight: Double = 1.0) :
    BlockProviderBuilder<Block>() {

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>): Block {
        return Block(id, mod, weight, extras, transformers)
    }
}