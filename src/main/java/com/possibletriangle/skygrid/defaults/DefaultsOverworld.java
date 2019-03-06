package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;

public class DefaultsOverworld extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {


        RandomCollection<BlockInfo> fluids = new RandomCollection<BlockInfo>()
                .add(1.4, new BlockInfo()
                        .add(Blocks.WATER)
                        .add(new ResourceLocation("biomesoplenty", "hot_spring_water"), 0.1)
                        .addAtAll(FRAME_BLOCK))
                .add(0.2, new BlockInfo()
                        .add(Blocks.WATER)
                        .add(new ResourceLocation("biomesoplenty", "hot_spring_water"), 0.1)
                        .addAt(EnumFacing.UP, Blocks.WATERLILY)
                        .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "waterlily"), 4)
                        .addAtAll(FRAME_BLOCK))
                .add(1, new BlockInfo().add(Blocks.LAVA).addAtAll(FRAME_BLOCK));

        RandomCollection<BlockInfo> containers = new RandomCollection<BlockInfo>()
                .add(0.2, new BlockInfo().add(Blocks.DROPPER))
                .add(0.2, new BlockInfo().add(Blocks.DISPENSER))
                .add(2, new BlockInfo().add(Blocks.CRAFTING_TABLE))
                .add(0.5, new BlockInfo().add(Blocks.HOPPER))
                .add(1, new BlockInfo().add(Blocks.FURNACE))
                .add(0.1, new BlockInfo().add(Blocks.NOTEBLOCK))
                .add(1, chest());

        RandomCollection<BlockInfo> tree = new RandomCollection<BlockInfo>()
                .add(1, logs())
                .add(0.5, planks())
                .add(1, leaves());

        RandomCollection<BlockInfo> ground = new RandomCollection<BlockInfo>()
                .add(3, rock())
                .add(1, dirt())
                .add(1, sand())
                .add(2, grass())
                .add(0.5, new BlockInfo().add(Blocks.CLAY))
                .add(0.1, new BlockInfo().add(Blocks.SNOW))
                .add(0.1, new BlockInfo().add(Blocks.ICE));

        RandomCollection<BlockInfo> stonebricks = new RandomCollection<BlockInfo>()
                .add(0.2, new BlockInfo().add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY)))
                .add(0.2, new BlockInfo().add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED)))
                .add(0.2, new BlockInfo().add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED)))
                .add(1, new BlockInfo().add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT)))
                .add(0.05, new BlockInfo().add(new ResourceLocation("thaumcraft", "stone_arcane:0")))
                .add(0.02, new BlockInfo().add(new ResourceLocation("thaumcraft", "stone_arcane:0"))
                .addAt(EnumFacing.UP, new ResourceLocation("thaumcraft", "nitor_orange")));

        RandomCollection<BlockInfo> prismarine = new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.ROUGH)))
                .add(0.4, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS)))
                .add(0.2, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK)))
                .add(0.3, new BlockInfo().add(Blocks.SEA_LANTERN));

        RandomCollection<BlockInfo> terracotta = new RandomCollection<BlockInfo>()
                .add(0.2, new BlockInfo().add(Blocks.PURPLE_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.BROWN_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.MAGENTA_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.PINK_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.GRAY_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.YELLOW_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.RED_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.ORANGE_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.GREEN_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.LIME_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.CYAN_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.BLUE_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.WHITE_GLAZED_TERRACOTTA))
                .add(0.2, new BlockInfo().add(Blocks.SILVER_GLAZED_TERRACOTTA))
                .add(0.3, new BlockInfo().add(Blocks.BLACK_GLAZED_TERRACOTTA));

        RandomCollection<BlockInfo> building = new RandomCollection<BlockInfo>()
                .add(0.2, stonebricks)
                .add(0.1, new BlockInfo().add(Blocks.WOOL))
                .add(0.1, new BlockInfo().add(Blocks.STAINED_HARDENED_CLAY))
                .add(0.1, terracotta)
                .add(0.1, new BlockInfo().add(Blocks.STAINED_GLASS))
                .add(0.3, new BlockInfo().add(Blocks.GLASS))
                .add(0.3, bookshelfs())
                .add(0.3, new BlockInfo().add(Blocks.MOSSY_COBBLESTONE))
                .add(0.1, new BlockInfo().add(Blocks.PUMPKIN))
                .add(0.1, new BlockInfo().add(Blocks.MELON_BLOCK))
                .add(0.02, new BlockInfo().add(Blocks.LIT_PUMPKIN))
                .add(0.1, new BlockInfo().add(Blocks.SLIME_BLOCK))
                .add(0.1, new BlockInfo().add(Blocks.HAY_BLOCK))
                .add(0.1, new BlockInfo().add(Blocks.LIT_REDSTONE_LAMP))
                .add(0.1, new BlockInfo().add(Blocks.WEB))
                .add(0.1, new BlockInfo().add(Blocks.PISTON.getDefaultState()))
                .add(0.2, new BlockInfo().add(Blocks.BRICK_BLOCK))

                .add(0.1, new BlockInfo().add(new ResourceLocation("quark", "sandy_bricks")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("quark", "iron_plate")));

        BlockInfo taint = new BlockInfo()
                .add(new ResourceLocation("thaumcraft", "taint_soil"))
                .add(new ResourceLocation("thaumcraft", "taint_geyser"))
                .add(new ResourceLocation("thaumcraft", "taint_log"));

        blocks.add(0.02, taint);
        blocks.add(10, tree);
        blocks.add(20, ground);
        blocks.add(8, ores());
        blocks.add(0.8, oreBlocks());
        blocks.add(1, fluids);
        blocks.add(1.5, containers);
        blocks.add(2, building);
        blocks.add(0.3, prismarine);
        blocks.add(0.2, new BlockInfo().add(new ResourceLocation("quark", "crystal")));
        blocks.add(0.4, new BlockInfo().add(new ResourceLocation("natura", "clouds:0")));
        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
        tables.add(8, LootTableList.CHESTS_SPAWN_BONUS_CHEST);
        tables.add(8, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
        tables.add(8, LootTableList.CHESTS_SIMPLE_DUNGEON);
        tables.add(2, LootTableList.CHESTS_ABANDONED_MINESHAFT);
        tables.add(2, LootTableList.CHESTS_DESERT_PYRAMID);
        tables.add(2, LootTableList.CHESTS_JUNGLE_TEMPLE);
        tables.add(4, LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
        tables.add(4, LootTableList.CHESTS_STRONGHOLD_CROSSING);
        tables.add(2, LootTableList.CHESTS_STRONGHOLD_LIBRARY);
        tables.add(1, LootTableList.CHESTS_WOODLAND_MANSION);
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("zombie"));
        mob.add(5, new ResourceLocation("skeleton"));
        mob.add(4, new ResourceLocation("spider"));
        mob.add(4, new ResourceLocation("cave_spider"));
        mob.add(1, new ResourceLocation("creeper"));
    }

    public static RandomCollection<BlockInfo> leaves() {
        return new RandomCollection<BlockInfo>()
                .add(4, new BlockInfo().add(Blocks.LEAVES))
                .add(2, new BlockInfo().add(Blocks.LEAVES2))

                .add(1, new BlockInfo().add(new ResourceLocation("natura", "overworld_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("natura", "overworld_leaves2")));
    }

    public static RandomCollection<BlockInfo> logs() {
        return new RandomCollection<BlockInfo>()
                .add(4, new BlockInfo().add(Blocks.LOG))
                .add(2, new BlockInfo().add(Blocks.LOG2))

                .add(1, new BlockInfo().add(new ResourceLocation("thaumcraft", "log_greatwood")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("thaumcraft", "log_silverwood")))

                .add(1, new BlockInfo().add(new ResourceLocation("natura", "overworld_logs")))
                .add(1, new BlockInfo().add(new ResourceLocation("natura", "overworld_logs2")));
    }

    public static RandomCollection<BlockInfo> planks() {
        return new RandomCollection<BlockInfo>()
                .add(6, new BlockInfo().add(Blocks.PLANKS))

                .add(1, new BlockInfo().add(new ResourceLocation("natura", "overworld_planks")));
    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollection<BlockInfo>()
                .add(12, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)))
                .add(2, new BlockInfo().add(Blocks.OBSIDIAN))
                .add(6, new BlockInfo().add(Blocks.COBBLESTONE))
                .add(8, new BlockInfo().add(Blocks.GRAVEL))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE)))
                .add(1, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE_SMOOTH)))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE)))
                .add(1, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH)))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE)))
                .add(1, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH)))

                .add(5, new BlockInfo().add("stoneBasalt"))
                .add(5, new BlockInfo().add("stoneMarble"))
                .add(5, new BlockInfo().add("stoneLimestone"));
    }

    public static RandomCollection<BlockInfo> sand() {
        return new RandomCollection<BlockInfo>()
                .add(10, new BlockInfo().add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND)))
                .add(10, new BlockInfo().add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND))
                        .addAt(EnumFacing.UP, Blocks.REEDS, 0.5)
                        .addAt(EnumFacing.UP, Blocks.CACTUS, 0.5)
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH), 0.2)
                        .addAt(EnumFacing.UP, new ResourceLocation("natura", "saguaro"), 0.2)
                        .addAt(EnumFacing.UP, new ResourceLocation("natura", "saguaro_baby"), 0.2))
                .add(10, new BlockInfo().add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND)))
                .add(10, new BlockInfo().add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND))
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH), 0.1))
                .add(3, new BlockInfo().add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT)))
                .add(1.5, new BlockInfo().add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED)))
                .add(1.5, new BlockInfo().add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)))
                .add(3, new BlockInfo().add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.DEFAULT)))
                .add(1.5, new BlockInfo().add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED)))
                .add(1.5, new BlockInfo().add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)));
    }

    public static RandomCollection<BlockInfo> dirt() {
        return new RandomCollection<BlockInfo>()
                .add(10, new BlockInfo().add(Blocks.DIRT))
                .add(3, new BlockInfo().add(Blocks.GRASS_PATH))
                .add(2, new BlockInfo().add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)));
    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollection<BlockInfo>()
                .add(10, new BlockInfo().add(Blocks.GRASS)
                    .addAt(EnumFacing.UP, Blocks.AIR, 0.25)
                    .addAt(EnumFacing.UP, new ResourceLocation("botania", "flower")))
                .add(1.5, new BlockInfo().add(Blocks.GRASS)
                    .addAt(EnumFacing.UP, Blocks.SAPLING, 2)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_0"))
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:0"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:1"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:2"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:3"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:4"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:5"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_1:6"), 1F/7)
                    .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "sapling_2"))
                    .addAt(EnumFacing.UP, new ResourceLocation("natura", "overworld_sapling"))
                    .addAt(EnumFacing.UP, new ResourceLocation("natura", "overworld_sapling2")))
                .add(1, new BlockInfo().add(Blocks.GRASS)
                        .addAt(EnumFacing.UP, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, 1)))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.FERN).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.FERN).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.ROSE).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.ROSE).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(0.8, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 10)
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 10))
                .add(10, new BlockInfo().add(Blocks.GRASS)
                        .addAt(EnumFacing.UP, Blocks.YELLOW_FLOWER, 1)
                        .addAt(EnumFacing.UP, Blocks.RED_FLOWER, 8)
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 1)
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), 1)
                        .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "flower_0"), 2)
                        .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "flower_1"), 2))
                .add(3, new BlockInfo().add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)))
                .add(1, new BlockInfo().add(Blocks.MYCELIUM)
                        .addAt(EnumFacing.UP, Blocks.AIR.getDefaultState(), 1)
                        .addAt(EnumFacing.UP, Blocks.RED_MUSHROOM, 1)
                        .addAt(EnumFacing.UP, Blocks.BROWN_MUSHROOM, 1));
    }

    public static RandomCollection<BlockInfo> chest() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.CHEST))
                .add(6, new BlockInfo().add(new ResourceLocation("quark", "custom_chest")));
    }

    public static RandomCollection<BlockInfo> bookshelfs() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.BOOKSHELF))
                .add(1, new BlockInfo().add("bookshelfSpruce"))
                .add(1, new BlockInfo().add("bookshelfBirch"))
                .add(1, new BlockInfo().add("bookshelfJungle"))
                .add(1, new BlockInfo().add("bookshelfAcacia"))
                .add(1, new BlockInfo().add("bookshelfDarkOak"));
        /*.add(1, new BlockInfo().add("bookshelf")); */
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.EMERALD_ORE))
                .add(2, new BlockInfo().add(Blocks.DIAMOND_ORE))
                .add(8, new BlockInfo().add(Blocks.REDSTONE_ORE))
                .add(6, new BlockInfo().add(Blocks.LAPIS_ORE))
                .add(3, new BlockInfo().add(Blocks.GOLD_ORE))
                .add(10, new BlockInfo().add(Blocks.IRON_ORE))
                .add(20, new BlockInfo().add(Blocks.COAL_ORE))

                .add(8, new BlockInfo().add("oreLead"))
                .add(8, new BlockInfo().add("oreTin"))
                .add(12, new BlockInfo().add("oreCopper"))
                .add(5, new BlockInfo().add("oreNickel"))
                .add(8, new BlockInfo().add("oreAluminum"))
                .add(3, new BlockInfo().add("oreSilver"))
                .add(3, new BlockInfo().add("oreClathrateRedstone"))

                .add(0.8, new BlockInfo().add("oreRuby"))
                .add(0.8, new BlockInfo().add("orePeridot"))
                .add(0.8, new BlockInfo().add("oreSapphire"))
                .add(4, new BlockInfo().add("oreAmber"))
                .add(4, new BlockInfo().add("oreCinnabar"));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.EMERALD_BLOCK))
                .add(2, new BlockInfo().add(Blocks.DIAMOND_BLOCK))
                .add(8, new BlockInfo().add(Blocks.REDSTONE_BLOCK))
                .add(6, new BlockInfo().add(Blocks.LAPIS_BLOCK))
                .add(3, new BlockInfo().add(Blocks.GOLD_BLOCK))
                .add(10, new BlockInfo().add(Blocks.IRON_BLOCK))
                .add(20, new BlockInfo().add(Blocks.COAL_BLOCK))

                .add(8, new BlockInfo().add("blockLead"))
                .add(8, new BlockInfo().add("blockTin"))
                .add(12, new BlockInfo().add("blockCopper"))
                .add(5, new BlockInfo().add("blockNickel"))
                .add(8, new BlockInfo().add("blockAluminum"))
                .add(3, new BlockInfo().add("blockSilver"))
                .add(3, new BlockInfo().add("oreClathrateRedstone"))

                .add(0.8, new BlockInfo().add("blockRuby"))
                .add(0.8, new BlockInfo().add("blockPeridot"))
                .add(0.8, new BlockInfo().add("blockSapphire"))
                .add(3, new BlockInfo().add("blockAmber"))

                .add(2, new BlockInfo().add("blockBrass"))
                .add(2, new BlockInfo().add("blockThaumium"))
                .add(1, new BlockInfo().add("blockVoid"))

                .add(1.5, new BlockInfo().add(new ResourceLocation("botania", "storage:0")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("botania", "storage:1")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("botania", "storage:2")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("botania", "storage:3")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("botania", "storage:4")));
    }

}
