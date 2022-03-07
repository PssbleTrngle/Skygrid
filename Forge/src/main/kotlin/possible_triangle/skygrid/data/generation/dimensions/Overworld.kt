package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.data.DataGenerator
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DripstoneThickness
import net.minecraft.world.level.dimension.LevelStem
import net.minecraftforge.common.Tags
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator
import possible_triangle.skygrid.data.generation.builder.ExceptFilterBuilder
import possible_triangle.skygrid.data.generation.builder.providers.TagBuilder

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Overworld(generator: DataGenerator) : DimensionConfigGenerator("overworld", generator) {

    override fun generate() {

        fun TagBuilder.overworldWood(builder: ExceptFilterBuilder.() -> Unit = {}) {
            except {
                pattern("crimson")
                pattern("warped")
                pattern("hellbark")
                mod("endergetic")
                mod("midnight")
                mod("botania")
                builder(this)
            }
        }

        dimension(LevelStem.OVERWORLD) {
            loot {
                table("chests/spawn_bonus_chest", weight = 20.0)
                table("chests/desert_pyramid", weight = 2.0)
                table("chests/woodland_mansion", weight = 2.0)
                table("chests/simple_dungeon")
                table("chests/abandoned_mineshaft")
                table("chests/buried_treasure")
                table("chests/igloo_chest")
                table("chests/jungle_temple")
                table("chests/pillager_outpost")
                table("chests/shipwreck_map")
                table("chests/shipwreck_supply")
                table("chests/shipwreck_treasure")
                table("chests/stronghold_corridor")
                table("chests/stronghold_crossing")
                table("chests/stronghold_library")
                table("chests/underwater_ruin_big")
                table("chests/underwater_ruin_small")
            }

            mobs {
                mob(EntityType.ZOMBIE, weight = 2.0)
                mob(EntityType.SKELETON, weight = 2.0)
                mob(EntityType.CAVE_SPIDER, weight = 3.0)
                mob(EntityType.HUSK)
                mob(EntityType.SPIDER)
                mob(EntityType.CREEPER)
            }

            blocks {
                list("fluids", weight = 2.0) {
                    block(Blocks.LAVA)
                    block(Blocks.WATER, weight = 3.0) {
                        side(UP, probability = 0.05) {
                            block(Blocks.LILY_PAD)
                        }
                    }
                }

                list("ground", weight = 100.0) {
                    list("stone", weight = 20.0) {
                        tag(BlockTags.BASE_STONE_OVERWORLD, weight = 8.0) {
                            side(UP, probability = 0.1) {
                                block("glow_shroom", "quark")
                            }
                        }
                        block(Blocks.GRAVEL, weight = 2.0)
                        block(Blocks.CLAY)
                    }

                    preset("moss", weight = 0.3) {
                        block(Blocks.MOSS_BLOCK) {
                            side(DOWN, probability = 0.1) {
                                block(Blocks.SPORE_BLOSSOM)
                            }
                            double(probability = 0.2) {
                                block(Blocks.BIG_DRIPLEAF)
                            }
                            side(UP, probability = 0.4) {
                                block(Blocks.BIG_DRIPLEAF)
                            }
                        }
                    }

                    preset("dripstone", weight = 0.3) {
                        listOf(UP, DOWN).forEach {
                            block(Blocks.DRIPSTONE_BLOCK) {
                                side(it) {
                                    block(Blocks.POINTED_DRIPSTONE) {
                                        property(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP)
                                        property(PointedDripstoneBlock.TIP_DIRECTION, it)
                                    }
                                    block(Blocks.POINTED_DRIPSTONE, weight = 0.5) {
                                        property(PointedDripstoneBlock.THICKNESS, DripstoneThickness.FRUSTUM)
                                        property(PointedDripstoneBlock.TIP_DIRECTION, it)
                                        side(it) {
                                            block(Blocks.POINTED_DRIPSTONE) {
                                                property(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP)
                                                property(PointedDripstoneBlock.TIP_DIRECTION, it)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    list("dirt", weight = 5.0) {
                        side(DOWN, probability = 0.2) {
                            block(Blocks.HANGING_ROOTS)
                        }

                        block(Blocks.DIRT)
                        block("mud", "biomesoplenty", weight = 0.1)
                        block(Blocks.DIRT_PATH, weight = 0.1)
                        block(Blocks.COARSE_DIRT, weight = 0.1)

                        list("grass", weight = 4.0) {
                            list("grass and fern") {
                                block(Blocks.GRASS_BLOCK) {
                                    side(UP) {
                                        block(Blocks.FERN)
                                        block(Blocks.GRASS)
                                    }
                                }
                                block(Blocks.GRASS_BLOCK, weight = 0.5) {
                                    double {
                                        block(Blocks.LARGE_FERN)
                                        block(Blocks.TALL_GRASS)
                                    }
                                }
                            }

                            preset("flowers") {
                                block(Blocks.GRASS_BLOCK) {
                                    side(UP) {
                                        tag("mystical_flowers", "botania", weight = 0.1)
                                        tag(BlockTags.SMALL_FLOWERS) {
                                            except {
                                                mod("botania")
                                                pattern("wither_rose")
                                            }
                                        }
                                    }
                                }
                                block(Blocks.GRASS_BLOCK, weight = 0.5) {
                                    double { tag(BlockTags.TALL_FLOWERS) }
                                }
                            }

                            block(Blocks.GRASS_BLOCK, weight = 0.5) {
                                side(UP, probability = 0.6) {
                                    tag(BlockTags.SAPLINGS)
                                }
                            }
                        }

                        block(Blocks.MYCELIUM) {
                            side(UP) {
                                block(Blocks.RED_MUSHROOM)
                                block(Blocks.BROWN_MUSHROOM)
                            }
                        }
                    }

                    block(Blocks.OBSIDIAN, weight = 0.5)

                    list("sand", weight = 5.0) {
                        block("sandy_bricks", "quark", weight = 0.1)
                        list(weight = 0.2) {
                            block(Blocks.SANDSTONE)
                            block(Blocks.CHISELED_SANDSTONE)
                            block(Blocks.SMOOTH_SANDSTONE)
                            block(Blocks.RED_SANDSTONE, weight = 0.2)
                            block(Blocks.CHISELED_RED_SANDSTONE, weight = 0.2)
                            block(Blocks.SMOOTH_RED_SANDSTONE, weight = 0.2)
                        }
                        list {
                            block(Blocks.SAND)
                            block(Blocks.RED_SAND, weight = 0.2)
                            side(UP, probability = 0.1) {
                                block(Blocks.CACTUS)
                                block(Blocks.DEAD_BUSH)
                            }
                        }
                    }

                    list("cold") {
                        block(Blocks.SNOW_BLOCK)
                        block(Blocks.ICE)
                        block(Blocks.PACKED_ICE, weight = 0.1)
                        block(Blocks.BLUE_ICE, weight = 0.02)
                        block("permafrost", "quark", weight = 0.05)
                    }
                }

                list("crops", weight = 9.0) {
                    block(Blocks.FARMLAND) {
                        side(UP) {
                            list {
                                cycle(CropBlock.AGE)
                                tag(BlockTags.CROPS)
                            }
                        }
                    }

                    block(Blocks.PODZOL, weight = 0.1) {
                        side(UP) { block(Blocks.BAMBOO_SAPLING) }
                    }

                    block(Blocks.JUNGLE_WOOD, weight = 0.01) {
                        side(NORTH) {
                            block(Blocks.COCOA) {
                                property(CocoaBlock.FACING, SOUTH)
                            }
                        }
                    }

                    block(Blocks.SAND, weight = 0.5) {
                        side(NORTH) { block(Blocks.WATER) }
                        double {
                            block(Blocks.SUGAR_CANE)
                        }
                    }

                    block(Blocks.GRASS_BLOCK, weight = 0.8) {
                        side(UP) {
                            list {
                                cycle(SweetBerryBushBlock.AGE)
                                block(Blocks.SWEET_BERRY_BUSH)
                            }
                        }
                    }

                    list("melons", weight = 0.2) {
                        block(Blocks.MELON)
                    }

                    list("pumpkins", weight = 0.2) {
                        block(Blocks.PUMPKIN)
                        block(Blocks.CARVED_PUMPKIN)
                    }
                }

                list("tree", weight = 50.0) {
                    tag(BlockTags.LOGS_THAT_BURN) {
                        cycle(BlockStateProperties.AXIS)
                        overworldWood {
                            pattern("stripped")
                            pattern("_wood")
                            mod("simplefarming")
                        }
                    }
                    block(Blocks.BEE_NEST, weight = 0.01)
                    tag(BlockTags.PLANKS, weight = 0.3) { overworldWood() }
                    tag(BlockTags.LEAVES, weight = 0.6) {
                        property(LeavesBlock.PERSISTENT, true)
                        overworldWood()
                    }
                    list("mushrooms blocks") {
                        block(Blocks.MUSHROOM_STEM)
                        block(Blocks.RED_MUSHROOM_BLOCK)
                        block(Blocks.BROWN_MUSHROOM_BLOCK)
                        block("glow_shroom_block", "quark", weight = 0.1)
                        block("glow_shroom_block", "biomesoplenty", weight = 0.1)
                    }
                }

                list("building", weight = 14.0) {
                    list("bricks") {
                        block(Blocks.COBBLESTONE)
                        block(Blocks.COBBLED_DEEPSLATE, weight = 0.5)
                        block(Blocks.MOSSY_COBBLESTONE, weight = 0.1)
                        block(Blocks.SMOOTH_STONE, weight = 0.1)

                        list("bricks") {
                            block(Blocks.BRICKS)
                            list("stone bricks") {
                                block(Blocks.STONE_BRICKS)
                                block(Blocks.MOSSY_STONE_BRICKS, weight = 0.5)
                                block(Blocks.CRACKED_STONE_BRICKS, weight = 0.2)
                                block(Blocks.CHISELED_STONE_BRICKS, weight = 0.2)
                            }
                            list("deepslate bricks") {
                                block(Blocks.DEEPSLATE_BRICKS)
                                block(Blocks.POLISHED_DEEPSLATE)
                                block(Blocks.CHISELED_DEEPSLATE)
                                block(Blocks.DEEPSLATE_TILES)
                                block(Blocks.CRACKED_DEEPSLATE_BRICKS, weight = 0.5)
                                block(Blocks.CRACKED_DEEPSLATE_TILES, weight = 0.5)
                            }
                            list("cobblestone bricks", weight = 0.2) {
                                block("cobblestone_bricks", "quark")
                                block("mossy_cobblestone_bricks", "quark", weight = 0.5)
                            }
                            list("quark bricks") {
                                block("polished_jasper_block", "quark", weight = 0.5)
                                block("polished_limestone_block", "quark", weight = 0.5)
                                block("polished_shale_block", "quark", weight = 0.5)
                            }
                        }
                    }

                    block(Blocks.BOOKSHELF, weight = 0.05)
                    tag(Tags.Blocks.GLASS_SILICA)
                    tag(BlockTags.WOOL)

                    list("midori", weight = 0.01) {
                        cycle(BlockStateProperties.AXIS)
                        block("midori_block", "quark")
                        block("midori_pillar", "quark")
                    }

                    list("redstone", weight = 0.1) {
                        block(Blocks.OBSERVER)
                        block(Blocks.DISPENSER)
                        block(Blocks.DROPPER)
                        block(Blocks.NOTE_BLOCK)
                        block(Blocks.PISTON)
                        block(Blocks.TARGET, weight = 0.5)
                        block(Blocks.STICKY_PISTON, weight = 0.2)
                        block(Blocks.HOPPER)
                        block(Blocks.REDSTONE_LAMP)
                        block(Blocks.TNT)
                        block(Blocks.SLIME_BLOCK, weight = 2.0)
                        block(Blocks.HONEY_BLOCK)
                        // TODO quark redstone
                    }

                    list("workstations", weight = 0.1) {
                        block(Blocks.CRAFTING_TABLE, weight = 20.0)
                        block(Blocks.FURNACE, weight = 3.0)
                        block(Blocks.SMOKER)
                        block(Blocks.BLAST_FURNACE)
                        block(Blocks.LOOM)
                        block(Blocks.WATER_CAULDRON)
                        block(Blocks.CARTOGRAPHY_TABLE)
                        block(Blocks.FLETCHING_TABLE)
                        block(Blocks.SMITHING_TABLE)
                        block(Blocks.STONECUTTER)
                        block(Blocks.LECTERN)
                        block(Blocks.COMPOSTER)
                    }

                    block(Blocks.COBWEB, weight = 0.02)
                }

                list("loot") {
                    tag(Tags.Blocks.BARRELS_WOODEN) { overworldWood() }
                    tag(Tags.Blocks.CHESTS_WOODEN) { overworldWood() }
                }

                block(Blocks.SPAWNER)

                preset("ocean") {
                    tag(BlockTags.CORAL_BLOCKS) {
                        side(UP) {
                            tag(BlockTags.CORAL_PLANTS) {
                                except { tag(BlockTags.WALL_CORALS) }
                            }
                        }
                    }

                    block(Blocks.SAND, weight = 0.05) {
                        side(UP) {
                            block(Blocks.TURTLE_EGG) {
                                cycle(TurtleEggBlock.EGGS)
                            }
                        }
                    }

                    list {
                        block(Blocks.SAND)
                        block(Blocks.GRAVEL)

                        double {
                            block(Blocks.TALL_SEAGRASS)
                        }
                        side(UP) {
                            block(Blocks.KELP)
                            block(Blocks.SEAGRASS)
                            block(Blocks.SEA_PICKLE) {
                                cycle(SeaPickleBlock.PICKLES)
                            }
                        }
                    }

                    list("prismarine") {
                        list {
                            block(Blocks.PRISMARINE)
                            block(Blocks.PRISMARINE_BRICKS)
                            block(Blocks.DARK_PRISMARINE)
                            block(Blocks.SEA_LANTERN)
                        }
                        list("elder prismarine", weight = 0.1) {
                            block("elder_prismarine", "quark")
                            block("elder_prismarine_bricks", "quark")
                            block("elder_sea_lantern", "quark")
                            block("dark_elder_prismarine", "quark")
                        }
                    }
                }

                preset("ores", weight = 30.0) {
                    list("ores", weight = 20.0) {
                        tag(BlockTags.COAL_ORES, weight = 10.0)
                        tag(BlockTags.IRON_ORES, weight = 8.0)
                        tag(BlockTags.COPPER_ORES, weight = 8.0)
                        tag(BlockTags.GOLD_ORES, weight = 6.0)
                        tag(BlockTags.DIAMOND_ORES)
                        tag(BlockTags.EMERALD_ORES, weight = 0.5)
                        tag(BlockTags.LAPIS_ORES, weight = 4.5)
                        tag(BlockTags.REDSTONE_ORES, weight = 6.0)
                    }

                    list("blocks") {
                        block(Blocks.COAL_BLOCK)
                        block(Blocks.RAW_IRON_BLOCK, weight = 0.5)
                        block(Blocks.RAW_COPPER_BLOCK, weight = 0.5)
                        block(Blocks.RAW_GOLD_BLOCK, weight = 0.3)
                        block(Blocks.DIAMOND_BLOCK, weight = 0.01)
                        block(Blocks.EMERALD_BLOCK, weight = 0.005)
                        block(Blocks.LAPIS_BLOCK, weight = 0.45)
                        block(Blocks.REDSTONE_BLOCK, weight = 0.6)
                    }

                    list("crystals") {
                        block(Blocks.AMETHYST_BLOCK)
                        block(Blocks.BUDDING_AMETHYST) {
                            Direction.values().forEach {
                                side(it, probability = 0.5) {
                                    tag(SkygridMod.AMETHYST_CLUSTERS) {
                                        property(BlockStateProperties.FACING, it)
                                    }
                                }
                            }
                        }
                        tag("corundum", "quark")
                    }
                }

                list("compressed", weight = 6.0) {
                    cycle(BlockStateProperties.AXIS)
                    list {
                        block(Blocks.HAY_BLOCK)
                        block("stick_block", "quark")
                        block("gunpowder_block", "quark")
                    }
                    list("leather", weight = 0.1) {
                        block("bonded_leather")
                        block("bonded_rabbit_hide", weight = 0.1)
                        block("bonded_ravager_hide", weight = 0.01)
                    }
                    list("plants") {
                        block("cactus_block", "quark")
                        block("sugarcane_block", "quark")
                        block("bamboo_block", "quark", weight = 0.1)
                        block("aloe_bundle", "atmospheric")
                        block("passion_vine_bundle", "atmospheric")
                        block("yucca_cask", "atmospheric")
                        block("cattail_seed_sack", "environmental")
                        block("thatch", "quark")
                        block("beachgrass_thatch", "upgrade_aquatic")
                        block("cattail_thatch", "environmental")
                        block(Blocks.DRIED_KELP_BLOCK)
                    }
                    list("crops") {
                        block("apple_crate", "quark")
                        block("potato_crate", "quark")
                        block("carrot_crate", "quark")
                        block("beetroot_crate", "quark")
                        block("berry_sack", "quark", weight = 0.1)
                        block("cocoa_beans_sack", "quark", weight = 0.1)
                        block("golden_apple_crate", "quark", weight = 0.01)
                        block("passionfruit_crate", "atmospheric")
                        block("shimmering_passionfruit_crate", "atmospheric", weight = 0.01)
                    }
                }
            }
        }
    }

}