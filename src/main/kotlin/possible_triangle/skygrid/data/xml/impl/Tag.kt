package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.Extra
import possible_triangle.skygrid.config.FilterOperator
import possible_triangle.skygrid.config.Transformer
import kotlin.random.Random

@Serializable
@SerialName("tag")
data class Tag(
    private val id: String,
    private val mod: String = "minecraft",
    @XmlSerialName("weight", "", "") private val tagWeight: Double = 1.0,
    private val random: Boolean = true,
    private val expand: Boolean = false,
    override val sides: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    val filters: List<FilterOperator> = listOf(),
) : BlockProvider() {

    @Transient
    private lateinit var blocks: List<Block>

    override val weight: Double
        get() = if (expand)
            blocks.size * tagWeight
        else tagWeight

    private val key
        get() = ResourceLocation(mod, if (id.startsWith("#")) id.substring(1) else id)

    override val name: String?
        get() = "#$key"

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        this.blocks = tags.getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(key).values.filter {
            filters.all { filter -> filter.test(it) }
        }
        return this.blocks.isNotEmpty()
    }

    override fun base(random: Random): Block {
        return if (this.random) this.blocks.random(random)
        else this.blocks.first()
    }
}