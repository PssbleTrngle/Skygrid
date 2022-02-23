package possible_triangle.skygrid.data.generation.builder

import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.generation.builder.providers.BlockBuilder
import possible_triangle.skygrid.data.generation.builder.providers.BlockListBuilder
import possible_triangle.skygrid.data.generation.builder.providers.BlockProviderBuilder
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.impl.Block as SingleBlock

open class BlocksBuilder {

    private val children = arrayListOf<BlockProvider>()

    fun build(): List<BlockProvider> {
        return children.toList()
    }

    fun block(block: Block, builder: BlockProviderBuilder<SingleBlock>.() -> Unit) {
        val key = block.registryName
        require(key != null)
        return block(key.path, key.namespace, builder)
    }

    fun block(id: String, mod: String = "minecraft", builder: BlockBuilder.() -> Unit = {}) {
        BlockBuilder(id, mod).also {
            builder(it)
            children.add(it.build())
        }
    }

    fun list(name: String? = null, builder: BlockListBuilder.() -> Unit = {}) {
        BlockListBuilder(name).also {
            builder(it)
            children.add(it.build())
        }
    }

}