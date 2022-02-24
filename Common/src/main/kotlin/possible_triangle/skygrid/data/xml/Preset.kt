package possible_triangle.skygrid.data.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
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
            return value.provider.validate(server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY),
                server.tags)
        }

    }

    override fun generate(random: Random, access: BlockAccess) {
        provider.generate(random, access)
    }

}


