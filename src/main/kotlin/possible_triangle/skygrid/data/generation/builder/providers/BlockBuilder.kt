package possible_triangle.skygrid.data.generation.builder

import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.BlockList

class BlockProviderListBuilder(private val name: String?) : BlockProviderBuilder<BlockList>() {

    private val children = BlockListBuilder()

    fun children(consumer: BlockListBuilder.() -> Unit) {
        consumer(children)
    }

    override fun buildWith(weight: Double, extras: List<Extra>, transformers: List<Transformer>): BlockList {
        return BlockList(children.build(), weight, name, extras, transformers)
    }
}