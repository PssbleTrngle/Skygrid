package possible_triangle.skygrid.data.generation.dimensions

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DripstoneThickness
import net.minecraft.world.level.dimension.LevelStem
import net.minecraftforge.common.Tags
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.data.generation.DimensionConfigGenerator
import possible_triangle.skygrid.data.generation.builder.IBlocksBuilder
import possible_triangle.skygrid.data.generation.builder.providers.BlockProviderBuilder

fun BlockProviderBuilder<*>.overworldWood() {
    except {
        pattern("crimson")
        pattern("warped")
        pattern("hellbark")
        mod("endergetic")
        mod("midnight")
        mod("botania")
        mod("simplefarming")
    }
}

fun BlockProviderBuilder<*>.cluster(crystals: IBlocksBuilder.() -> BlockProviderBuilder<*>) {
    Direction.values().forEach { direction ->
        side(direction, probability = 0.5) {
            crystals(this).also {
                it.property(BlockStateProperties.FACING, direction)
            }
        }
    }
}

@ExperimentalXmlUtilApi
@ExperimentalSerializationApi
class Overworld(generator: DataGenerator) : DimensionConfigGenerator("overworld", generator) {

    override fun generate() {

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
                        tag(BlockTags.BASE_STONE_OVERWORLD, expand = true) {
                            side(UP, probability = 0.02) {
                                block("glow_shroom", "quark")
                            }
                        }
                        block(Blocks.STONE)
                        block("limestone", mod = "create")
                        block("ochrum", mod = "create")
                        block("scoria", mod = "create")
                        block("scorchia", mod = "create")
                        block(Blocks.CALCITE, weight = 0.1)
                    }

                    block(Blocks.GRAVEL, weight = 10.0)
                    block(Blocks.CLAY, weight = 8.0)

                    preset("moss", weight = 0.3) {
                        block(Blocks.MOSS_BLOCK) {
                            side(DOWN, probability = 0.1) {
                                block(Blocks.SPORE_BLOSSOM)
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
                                        block("bush", mod = "biomesoplenty")
                                        block("sprout", mod = "biomesoplenty")
                                        block("clover", mod = "biomesoplenty", weight = 0.1)
                                    }
                                }
                                block(Blocks.GRASS_BLOCK, weight = 0.5) {
                                    double {
                                        block(Blocks.LARGE_FERN)
                                        block(Blocks.TALL_GRASS)
                                        block("barley", mod = "biomesoplenty", weight = 0.2)
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
                                                pattern("burning_blossom")
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
                        list("sandstone", weight = 0.2) {
                            list("normal sandstone") {
                                block(Blocks.SANDSTONE)
                                block(Blocks.CHISELED_SANDSTONE)
                                block(Blocks.CUT_SANDSTONE)
                                block("sandstone_bricks", mod = "quark")
                            }
                            list("red sandstone", weight = 0.2) {
                                block("red_sandstone_bricks", mod = "quark")
                                block(Blocks.RED_SANDSTONE, weight = 0.2)
                                block(Blocks.CHISELED_RED_SANDSTONE, weight = 0.2)
                                block(Blocks.CUT_RED_SANDSTONE, weight = 0.2)
                            }
                        }
                        list {
                            block(Blocks.SAND)
                            block(Blocks.RED_SAND, weight = 0.2)
                            side(UP, probability = 0.1) {
                                block(Blocks.CACTUS)
                                block(Blocks.DEAD_BUSH)
                                block("desert_grass", mod = "biomesoplenty")
                                block("", mod = "biomesoplenty")
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
                            block("small_banana_fond", mod = "neapolitan", weight = 0.1)
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
                    overworldWood()
                    tag(BlockTags.LOGS_THAT_BURN) {
                        cycle(BlockStateProperties.AXIS)
                        except {
                            pattern("stripped")
                            pattern("_wood")
                        }
                    }
                    block(Blocks.BEE_NEST, weight = 0.01)
                    tag(BlockTags.PLANKS, weight = 0.3)
                    tag(BlockTags.LEAVES, weight = 0.6) {
                        property(LeavesBlock.PERSISTENT, true)
                    }
                    list("mushrooms blocks", weight = 0.01) {
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

                        list("copper") {
                            block(Blocks.WAXED_COPPER_BLOCK)
                            block(Blocks.WAXED_OXIDIZED_COPPER)
                            block(Blocks.WAXED_EXPOSED_COPPER)
                            block(Blocks.WAXED_WEATHERED_COPPER)
                            block(Blocks.WAXED_CUT_COPPER)
                            block(Blocks.WAXED_OXIDIZED_CUT_COPPER)
                            block(Blocks.WAXED_EXPOSED_CUT_COPPER)
                            block(Blocks.WAXED_WEATHERED_CUT_COPPER)
                        }

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
                            list("stone variants bricks") {
                                cycle(BlockStateProperties.AXIS)
                                listOf("quark:jasper",
                                    "quark:shale",
                                    "quark:limestone",
                                    "minecraft:granite",
                                    "minecraft:diorite",
                                    "minecraft:andesite",
                                    "quark:calcite",
                                    "quark:dripstone").map(::ResourceLocation)
                                    .forEach {
                                        block("polished_${it.path}", mod = it.namespace)
                                        block("${it.path}_pillar", mod = "quark")
                                        block("${it.path}_bricks", mod = "quark")
                                        block("chiseled_${it.path}_bricks", mod = "quark")
                                    }
                            }
                        }
                    }

                    fallback(weight = 0.05) {
                        tag("bookshelves", mod = "forge")
                        block(Blocks.BOOKSHELF)
                    }
                    tag(Tags.Blocks.GLASS_SILICA)
                    tag(BlockTags.WOOL)

                    block("bamboo_mat", mod = "quark", weight = 0.05)

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
                        block("iron_plate", mod = "quark", weight = 0.3)
                        block("rusty_iron_plate", mod = "quark", weight = 0.3)
                        fallback {
                            block("ender_watcher", mod = "quark")
                            block("ender_eye_block", mod = "botania")
                        }
                    }

                    list("workstations", weight = 0.1) {
                        block(Blocks.CRAFTING_TABLE, weight = 20.0)
                        list(weight = 3.0) {
                            block(Blocks.FURNACE)
                            block("deepslate_furnace", mod = "quark")
                        }
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
                    overworldWood()
                    tag(Tags.Blocks.BARRELS_WOODEN)
                    tag(Tags.Blocks.CHESTS_WOODEN)
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
                        tag("ores/zinc", mod = "forge", weight = 8.0)
                        except {
                            tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
                            pattern("nether_")
                        }
                    }

                    list("blocks", weight = 0.1) {
                        block(Blocks.COAL_BLOCK)
                        block(Blocks.RAW_IRON_BLOCK, weight = 10.0)
                        block(Blocks.RAW_COPPER_BLOCK, weight = 8.0)
                        block(Blocks.RAW_GOLD_BLOCK, weight = 6.0)
                        block(Blocks.DIAMOND_BLOCK)
                        block(Blocks.EMERALD_BLOCK, weight = 0.5)
                        block(Blocks.LAPIS_BLOCK, weight = 4.5)
                        block(Blocks.REDSTONE_BLOCK, weight = 6.0)
                        tag("storage_blocks/raw_zinc", mod = "forge", weight = 8.0)
                    }

                    list("crystals") {
                        block(Blocks.AMETHYST_BLOCK).cluster {
                            tag(SkygridMod.AMETHYST_CLUSTERS)
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
                        block("glowberry_sack", "quark")
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
                        block("apple_crate", "quark", weight = 0.3)
                        block("potato_crate", "quark")
                        block("carrot_crate", "quark")
                        block("beetroot_crate", "quark")
                        block("berry_sack", "quark", weight = 0.1)
                        block("cocoa_beans_sack", "quark", weight = 0.1)
                        block("golden_apple_crate", "quark", weight = 0.01)

                        block("passionfruit_crate", "atmospheric")
                        block("shimmering_passionfruit_crate", "atmospheric", weight = 0.01)

                        block("chocolate_block", mod = "neapolitan", weight = 0.1)
                        block("banana_crate", mod = "neapolitan", weight = 0.5)
                        block("strawberry_crate", mod = "neapolitan")
                        block("white_strawberry_crate", mod = "neapolitan", weight = 0.1)
                        block("mint_basket", mod = "neapolitan", weight = 0.5)
                        block("adzuki_crate", mod = "neapolitan", weight = 0.2)
                        block("vanilla_pod_block", mod = "neapolitan", weight = 0.3)
                    }
                    list("petals", weight = 0.1) {
                        DyeColor.values().forEach {
                            block("${it.name.lowercase()}_petal_block", mod = "botania")
                        }
                    }
                }
            }
        }
    }

}