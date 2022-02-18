package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.ProxyProvider
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
@SerialName("reference")
class Reference(
    private val id: String,
    override val weight: Double = 1.0,
    override val sides: List<Side> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) :
    ProxyProvider() {

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return false
    }

    override fun get(random: Random): BlockProvider {
        TODO()
    }
}