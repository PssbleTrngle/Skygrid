package possible_triangle.skygrid.config.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.config.BlockProvider
import possible_triangle.skygrid.config.Extra
import possible_triangle.skygrid.config.ProxyProvider
import possible_triangle.skygrid.config.Transformer
import possible_triangle.skygrid.util.WeightedList
import kotlin.random.Random

@Serializable
@SerialName("list")
data class BlockList(
    val children: List<BlockProvider>,
    override val weight: Double = 1.0,
    override val name: String? = null,
    override val sides: List<Extra> = listOf(),
    override val transformers: List<Transformer> = listOf(),
) : ProxyProvider() {

    @Transient
    private lateinit var validChildren: WeightedList<BlockProvider>

    override fun internalValidate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        SkygridMod.LOGGER.debug("Validated list $name")
        validChildren = WeightedList(children.filter { it.validate(blocks, tags) })
        return validChildren.isNotEmpty()
    }

    override fun get(random: Random): BlockProvider {
        return validChildren.random(random)
    }

    operator fun plus(other: BlockList): BlockList {
        return other.copy(children = this.children + other.children,
            sides = this.sides + other.sides,
            transformers = this.transformers + other.transformers
        )
    }

}