package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.FilterOperator
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@ExperimentalSerializationApi
@Serializable
@SerialName("tag")
data class Tag(
    private val id: String,
    private val mod: String = "minecraft",
    override val weight: Double = 1.0,
    private val random: Boolean = true,
    override val sides: List<Side> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    val filters: List<FilterOperator> = listOf(),
) : BlockProvider() {

    @Transient
    private lateinit var blocks: List<Block>

    private val key
        get() = ResourceLocation(mod, if (id.startsWith("#")) id.substring(1) else id)

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        this.blocks = tags.getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(key).values.filter {
            filters.all { filter -> filter.test(it) }
        }
        return this.blocks.isNotEmpty()
    }

    override fun base(random: Random, blocks: Registry<Block>): Block {
        return if (this.random) this.blocks.random(random)
        else this.blocks.first()
    }
}