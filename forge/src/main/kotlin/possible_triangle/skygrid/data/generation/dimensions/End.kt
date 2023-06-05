package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction.UP
import net.minecraft.data.DataGenerator
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS
import net.minecraft.world.level.dimension.LevelStem
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator
import possible_triangle.skygrid.data.xml.Distance

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class End(generator: DataGenerator) : DimensionConfigGenerator("end", generator) {

    override fun generate() {

        dimension(LevelStem.END) {
            distance = Distance.of(5)

            mobs {
                mob(EntityType.ENDERMAN)
                mob(EntityType.SHULKER, weight = 0.1)
            }

            loot {
                table("chests/end_city_treasure")
            }

            blocks {
                list("ground") {
                    block(Blocks.END_STONE) {
                        side(UP, probability = 0.1) {
                            block("chorus_weeds", mod = "quark")
                            block("chorus_twist", mod = "quark")
                        }
                    }
                    block(Blocks.END_STONE, weight = 0.05) {
                        side(UP) {
                            block(Blocks.CHORUS_FLOWER)
                        }
                    }
                }

                list("ores", weight = 0.2) {
                    block("biotite_ore", "quark")
                }

                list("building") {
                    cycle(AXIS)
                    list("obsidian", weight = 0.1) {
                        block(Blocks.OBSIDIAN)
                        block(Blocks.CRYING_OBSIDIAN)
                    }
                    block(Blocks.END_STONE_BRICKS)

                    list("purpur") {
                        block(Blocks.PURPUR_PILLAR)
                        block(Blocks.PURPUR_BLOCK)
                    }

                    list("duskbound") {
                        block("duskbound_block", mod = "quark")
                        block("duskbound_lantern", mod = "quark")
                    }

                    list("myalite") {
                        block("myalite", mod = "quark", weight = 10.0)
                        block("myalite_crystal", mod = "quark", weight = 5.0)
                        block("dusky_myalite", mod = "quark")
                        block("myalite_bricks", mod = "quark")
                        block("chiseled_myalite_bricks", mod = "quark")
                        block("myalite_pillar", mod = "quark")
                    }

                    block(Blocks.ENDER_CHEST, weight = 0.01)
                }

                list("compressed", weight = 0.05) {
                    block("chorus_fruit_block", mod = "quark")
                }

                list("loot", weight = 0.01) {
                    block(Blocks.SHULKER_BOX, weight = 0.0075)
                    fallback {
                        block("purpur_chest", mod = "quark")
                        block(Blocks.CHEST)
                    }
                }
            }
        }

    }

}