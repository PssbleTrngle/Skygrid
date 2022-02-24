package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator
import possible_triangle.skygrid.data.xml.Distance

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Cave(generator: DataGenerator) : DimensionConfigGenerator("cave", generator) {

    override fun generate() {

        dimension(ResourceLocation(SkygridMod.MOD_ID, "cave")) {
            distance = Distance.of(3)
            blocks {
                reference("ores", weight = 0.1)
                reference("dripstone", weight = 0.1)
                reference("moss", weight = 0.05)
                block(Blocks.OBSIDIAN, weight = 0.1)
                list("fluids", weight = 0.05) {
                    block(Blocks.LAVA)
                    block(Blocks.WATER)
                }
                tag(BlockTags.BASE_STONE_OVERWORLD) {
                    side(Direction.UP, probability = 0.1) {
                        block("glow_shroom", "quark")
                    }
                }
            }
        }

    }

}