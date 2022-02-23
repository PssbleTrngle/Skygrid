package possible_triangle.skygrid.data.generation.builder

import possible_triangle.skygrid.data.xml.BlockProvider

class BasicBlocksBuilder : IBlocksBuilder {

    private val children = arrayListOf<BlockProvider>()

    fun build(): List<BlockProvider> {
        return children.toList()
    }

    override fun add(block: BlockProvider) {
        children.add(block)
    }

}