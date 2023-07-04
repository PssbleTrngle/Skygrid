package possible_triangle.skygrid.world

import net.minecraft.world.level.levelgen.presets.WorldPreset

class SkygridPreset : WorldPreset(null) {

    override fun createWorldGenSettings(
        seed: Long,
        generateStructures: Boolean,
        bonuesChest: Boolean,
    ) = SkygridChunkGenerator.createSettings(seed, bonuesChest)

}