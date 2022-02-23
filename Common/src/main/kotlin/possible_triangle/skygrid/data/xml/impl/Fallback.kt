package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.ProxyProvider
import possible_triangle.skygrid.data.xml.Transformer
import kotlin.random.Random

@Serializable
@SerialName("fallback")
data class Fallback(
    override val name: String? = null,
    val children: List<BlockProvider>,
    override val weight: Double = 1.0,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) : ProxyProvider() {

    @Transient
    private lateinit var provider: BlockProvider

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        provider = children.firstOrNull { it.validate(blocks, tags) } ?: return false
        return true
    }

    override fun get(random: Random): BlockProvider {
        return provider
    }

}