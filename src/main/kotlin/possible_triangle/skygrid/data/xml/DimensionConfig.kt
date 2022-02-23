package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import net.minecraftforge.event.server.ServerAboutToStartEvent
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.data.xml.impl.LootTable
import possible_triangle.skygrid.world.BlockAccess
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import kotlin.random.Random
import possible_triangle.skygrid.data.xml.impl.Block as SingleBlock

@Serializable
@SerialName("dimension")
data class DimensionConfig(
    @XmlSerialName("blocks", "", "") val blocks: ListWrapper<BlockProvider>,
    @XmlSerialName("loot", "", "") val loot: ListWrapper<LootTable> = ListWrapper(),
    val replace: Boolean = false,
    val minY: Int = Int.MIN_VALUE,
    val maxY: Int = 100,
    val distance: Distance = Distance(4, 4, 4),
) {

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return this.blocks.validate { it.validate(blocks, tags) }
    }

    fun generate(random: Random, access: BlockAccess) {
        this.blocks.random(random).generate(random, access)
    }

    companion object : XMLResource<DimensionConfig>("dimensions", { DimensionConfig.serializer() }) {

        val DEFAULT = DimensionConfig(ListWrapper(SingleBlock("bedrock")))

        override fun merge(a: DimensionConfig, b: DimensionConfig): DimensionConfig {
            return if (b.replace) b
            else b.copy(
                replace = false,
                blocks = a.blocks + b.blocks,
                loot = a.loot + b.loot,
            )
        }

        init {
            FORGE_BUS.addListener { event: ServerAboutToStartEvent ->
                validate(DEFAULT, event.server)
            }
        }

        override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
            return value.validate(server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY), server.tags)
        }

    }

}