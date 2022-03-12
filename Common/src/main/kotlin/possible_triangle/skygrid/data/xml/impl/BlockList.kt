package possible_triangle.skygrid.data.xml.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.xml.*
import possible_triangle.skygrid.util.WeightedList
import kotlin.random.Random

@Serializable
@SerialName("list")
data class BlockList(
    override val weight: Double = 1.0,
    override val name: String? = null,
    override val extras: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
    override val filters: List<FilterOperator> = listOf(),
    val children: List<BlockProvider>,
) : ProxyProvider() {

    @Transient
    private lateinit var validChildren: WeightedList<BlockProvider>

    override fun flat(): List<Pair<Block, Double>> = validChildren.flat()

    override fun internalValidate(
        blocks: Registry<Block>,
        tags: TagContainer,
        references: ReferenceContext,
        filters: List<FilterOperator>,
    ): Boolean {
        SkygridMod.LOGGER.debug("Validated list $name")
        validChildren = WeightedList(children.filter { it.validate(blocks, tags, references, filters) })
        return validChildren.isNotEmpty()
    }

    override fun get(random: Random): BlockProvider {
        return validChildren.random(random)
    }

}