package possible_triangle.skygrid.builder

import net.minecraft.core.RegistryAccess
import possible_triangle.skygrid.builder.providers.BlockProviderBuilder
import possible_triangle.skygrid.data.xml.BlockProvider

class BasicBlocksBuilder(override val registries: RegistryAccess) : IBlocksBuilder {

    private val children = arrayListOf<BlockProviderBuilder<*>>()

    fun each(builder: BlockProviderBuilder<*>.() -> Unit = {}) {
        children.forEach(builder)
    }

    fun build(additional: BlockProviderBuilder<*>.() -> Unit = {}): List<BlockProvider> {
        return children.map { it.also(additional).build() }
    }

    override fun add(block: BlockProviderBuilder<*>) {
        children.add(block)
    }

}