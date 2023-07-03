package possible_triangle.skygrid.forge.world

import net.minecraft.world.level.levelgen.presets.WorldPreset
import possible_triangle.skygrid.world.SkygridChunkGenerator

class SkygridPreset : WorldPreset(null) {

    override fun createWorldGenSettings(
        seed: Long,
        generateStructures: Boolean,
        bonuesChest: Boolean,
    ) = SkygridChunkGenerator.createSettings(seed, bonuesChest)

}