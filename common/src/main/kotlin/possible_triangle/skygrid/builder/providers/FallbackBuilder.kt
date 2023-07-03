package possible_triangle.skygrid.builder.providers

import net.minecraft.core.RegistryAccess
import possible_triangle.skygrid.builder.BasicBlocksBuilder
import possible_triangle.skygrid.builder.IBlocksBuilder
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.data.xml.impl.Fallback

class FallbackBuilder(
    private val name: String?,
    private val weight: Double = 1.0,
    override val registries: RegistryAccess,
) : BlockProviderBuilder<Fallback>(), IBlocksBuilder {

    private val children = BasicBlocksBuilder(registries)

    override fun add(block: BlockProviderBuilder<*>) = children.add(block)

    override fun buildWith(extras: List<Extra>, transformers: List<Transformer>, filters: List<FilterOperator>): Fallback {
        return Fallback(name, weight, extras, transformers, filters, children.build())
    }

}