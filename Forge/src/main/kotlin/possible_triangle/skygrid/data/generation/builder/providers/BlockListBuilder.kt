package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.generation.builder.BasicBlocksBuilder
import possible_triangle.skygrid.data.generation.builder.IBlocksBuilder
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.BlockList

class BlockListBuilder(
        private val name: String?,
        private val weight: Double = 1.0,
) : BlockProviderBuilder<BlockList>(), IBlocksBuilder {

    private val children = BasicBlocksBuilder()

    override fun add(block: BlockProviderBuilder<*>) = children.add(block)

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>): BlockList {
        return BlockList(weight, name, extras, transformers, children.build())
    }

}