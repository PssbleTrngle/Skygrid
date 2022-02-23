package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Preset
import possible_triangle.skygrid.data.xml.ProxyProvider
import possible_triangle.skygrid.data.xml.Transformer
import kotlin.random.Random

@Serializable
@SerialName("reference")
class Reference(
    private val id: String,
    override val weight: Double = 1.0,
    override val extras: List<Side> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) :
    ProxyProvider() {

    override val name: String
        get() = key.toString()

    private val key
        get() = ResourceLocation(id)

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return Preset.exists(key)
    }

    override fun get(random: Random): BlockProvider {
        return Preset[key]?.provider ?: throw NullPointerException("Preset $key does not exist")
    }
}