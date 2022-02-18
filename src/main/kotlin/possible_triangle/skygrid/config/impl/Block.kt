package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
@SerialName("block")
class Block(
    private val id: String,
    private val mod: String = "minecraft",
    override val weight: Double = 1.0,
    override val sides: List<Side> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) : BlockProvider() {

    private val key
        get() = ResourceLocation(mod, id)

    override fun base(random: Random, blocks: Registry<Block>): Block {
        return blocks.get(key) ?: throw NullPointerException("Block not found: $key")
    }

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return blocks.containsKey(key)
    }
}