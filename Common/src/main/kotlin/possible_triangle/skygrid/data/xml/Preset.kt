package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
import possible_triangle.skygrid.data.ReferenceContext
import possible_triangle.skygrid.data.XMLResource
import possible_triangle.skygrid.world.BlockAccess
import possible_triangle.skygrid.world.Generator
import kotlin.random.Random

@Serializable
@SerialName("preset")
data class Preset(val provider: BlockProvider) : Generator<BlockAccess> {

    companion object : XMLResource<Preset>("presets", { Preset.serializer() }) {

        override fun merge(a: Preset, b: Preset): Preset {
            return b
        }

        override fun validate(value: Preset, server: MinecraftServer): Boolean {
            val references = ReferenceContext()
            val blocks = server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY)
            return value.provider.validate(blocks, server.tags, references, )
        }

    }

    override fun generate(random: Random, access: BlockAccess): Boolean {
        return provider.generate(random, access, )
    }

}


