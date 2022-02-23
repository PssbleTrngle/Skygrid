package possible_triangle.skygrid.data.generation

import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.BlockTagsProvider
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper
import possible_triangle.skygrid.Constants
import possible_triangle.skygrid.Constants.MOD_ID

class SkygridTags(generator: DataGenerator, files: ExistingFileHelper) :
    BlockTagsProvider(generator, MOD_ID, files) {

    override fun addTags() {

        tag(Constants.AMETHYST_CLUSTERS).add(Blocks.AMETHYST_CLUSTER,
            Blocks.SMALL_AMETHYST_BUD,
            Blocks.MEDIUM_AMETHYST_BUD,
            Blocks.LARGE_AMETHYST_BUD
        )

        tag(Constants.LOOT_CONTAINERS)
            .add(Blocks.CHEST)
            .add(Blocks.DISPENSER)
            .add(Blocks.DROPPER)
            .add(Blocks.BARREL)
            .addOptionalTag(Tags.Blocks.BARRELS.name)
            .addOptionalTag(Tags.Blocks.CHESTS.name)

    }

}