package possible_triangle.skygrid.data.generation.builder.providers

import possible_triangle.skygrid.data.generation.builder.BasicBlocksBuilder
import possible_triangle.skygrid.data.generation.builder.IBlocksBuilder
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.BlockList

class BlockListBuilder(private val name: String?) : BlockProviderBuilder<BlockList>(), IBlocksBuilder {

    private val children = BasicBlocksBuilder()

    override fun add(block: BlockProvider) = children.add(block)

    override fun buildWith(weight: Double, extras: List<Extra>, transformers: List<Transformer>): BlockList {
        return BlockList(children.build(), weight, name, extras, transformers)
    }

}