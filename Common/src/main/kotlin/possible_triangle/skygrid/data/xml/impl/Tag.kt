package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.FilterOperator
import possible_triangle.skygrid.data.xml.Transformer
import possible_triangle.skygrid.keyFrom
import kotlin.random.Random

@Serializable
@SerialName("tag")
data class Tag(
    private val id: String,
    private val mod: String? = null,
    @XmlSerialName("weight", "", "") private val tagWeight: Double = 1.0,
    private val random: Boolean = true,
    private val expand: Boolean = false,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    val filters: List<FilterOperator> = listOf(),
) : BlockProvider() {

    @Transient
    private lateinit var blocks: List<Block>

    @Transient
    private val key = keyFrom(id, mod)

    override val weight: Double
        get() = if (expand)
            blocks.size * tagWeight
        else tagWeight

    override val name: String
        get() = "#$key"

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer, references: ReferenceContext): Boolean {
        this.blocks = tags.getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(key).values.filter {
            filters.all { filter -> filter.test(it, blocks, tags) }
        }
        return this.blocks.isNotEmpty()
    }

    override fun base(random: Random): Block {
        return if (this.random) this.blocks.random(random)
        else this.blocks.first()
    }
}