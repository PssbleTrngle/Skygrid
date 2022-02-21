package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.Extra
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@Serializable
@SerialName("block")
class Block(
    private val id: String,
    private val mod: String = "minecraft",
    override val weight: Double = 1.0,
    override val sides: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) : BlockProvider() {

    @Transient
    lateinit var block: Block

    override val name: String
        get() = key.toString()

    private val key
        get() = ResourceLocation(mod, id)

    override fun base(random: Random): Block {
        return block
    }

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return blocks.getOptional(key).map {
            block = it
        }.isPresent
    }
}