package possible_triangle.skygrid.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.Registry
import net.minecraft.server.MinecraftServer
import possible_triangle.skygrid.data.XMLResource

@Serializable
@SerialName("preset")
data class Preset(val provider: BlockProvider) {

    companion object : XMLResource<Preset>("presets", { Preset.serializer() }) {

        override fun merge(a: Preset, b: Preset): Preset {
            return b
        }

        override fun validate(value: Preset, server: MinecraftServer): Boolean {
            return value.provider.validate(server.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY),
                server.tags)
        }

    }
}


