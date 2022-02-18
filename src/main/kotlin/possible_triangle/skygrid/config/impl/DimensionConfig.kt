package possible_triangle.skygrid.config.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import possible_triangle.skygrid.data.XMLResource

@ExperimentalSerializationApi
@Serializable
@SerialName("dimension")
data class DimensionConfig(
    @XmlSerialName("blocks", "", "") val blocks: BlockList,
    val replace: Boolean = true,
    val margin: Int = 4,
) {

    fun validate(blocks: Registry<Block>, tags: TagContainer): Boolean {
        return this.blocks.validate(blocks, tags)
    }

    companion object : XMLResource<DimensionConfig>("dimensions", { DimensionConfig.serializer() }) {

        val DEFAULT = DimensionConfig(BlockList(listOf(Block("bedrock"))))

        override fun merge(a: DimensionConfig, b: DimensionConfig): DimensionConfig {
            return b.copy(
                replace = a.replace || b.replace,
                blocks = a.blocks + b.blocks,
            )
        }

        override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
            return value.validate(server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY), server.tags)
        }

    }

}