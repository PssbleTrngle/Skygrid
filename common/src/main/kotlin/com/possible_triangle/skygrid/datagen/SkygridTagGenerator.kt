package com.possible_triangle.skygrid.datagen

import com.possible_triangle.skygrid.api.SkygridTags
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.BlockTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks

class SkygridTagGenerator(generator: DataGenerator) :
    BlockTagsProvider(generator) {

    override fun addTags() {
        fun forge(path: String) = ResourceLocation("forge", path)
        fun fabric(path: String) = ResourceLocation("c", path)

        tag(SkygridTags.AMETHYST_CLUSTERS).add(Blocks.AMETHYST_CLUSTER,
            Blocks.SMALL_AMETHYST_BUD,
            Blocks.MEDIUM_AMETHYST_BUD,
            Blocks.LARGE_AMETHYST_BUD
        )

        tag(SkygridTags.LOOT_CONTAINERS)
            .add(Blocks.CHEST)
            .add(Blocks.DISPENSER)
            .add(Blocks.DROPPER)
            .add(Blocks.BARREL)
            .addOptionalTag(forge("barrels"))
            .addOptionalTag(forge("chests"))
            .addOptionalTag(fabric("barrels"))
            .addOptionalTag(fabric("chests"))

        tag(SkygridTags.CHESTS)
            .add(Blocks.CHEST)
            .addOptionalTag(forge("chests/wooden"))
            .addOptionalTag(fabric("wooden_chests"))

        tag(SkygridTags.BARRELS)
            .add(Blocks.BARREL)
            .addOptionalTag(forge("barrels/wooden"))
            .addOptionalTag(fabric("wooden_barrels"))

        tag(SkygridTags.GLASS_SILICA)
            .addOptionalTag(forge("glass/silica"))
            .addOptionalTag(fabric("glass_blocks"))

        tag(SkygridTags.FOREIGN_ORES)
            .addOptionalTag(forge("ores_in_ground/netherrack"))
            .addOptionalTag(forge("ores_in_ground/endstone"))

    }

}