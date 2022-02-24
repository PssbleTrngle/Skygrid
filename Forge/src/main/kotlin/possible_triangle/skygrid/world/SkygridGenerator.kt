package possible_triangle.skygrid.world

import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.WorldGenSettings
import net.minecraftforge.common.world.ForgeWorldPreset

class SkygridGenerator : ForgeWorldPreset(null) {

    override fun createChunkGenerator(registries: RegistryAccess, seed: Long, settings: String): ChunkGenerator {
        return SkygridChunkGenerator.create(registries, seed, null)
    }

    override fun createSettings(
        registries: RegistryAccess,
        seed: Long,
        generateStructures: Boolean,
        bonusChest: Boolean,
        generatorSettings: String,
    ): WorldGenSettings = SkygridChunkGenerator.createSettings(registries, seed, bonusChest)

}