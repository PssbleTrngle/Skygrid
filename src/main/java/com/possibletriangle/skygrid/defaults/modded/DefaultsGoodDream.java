package com.possibletriangle.skygrid.defaults.modded;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsGoodDream extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> ground = new RandomCollectionJson<>(BlockInfo.class)
                .add(3, grass())
                .add(0.5, rock());

        blocks.add(2, ores());
        blocks.add(10, trees());
        blocks.add(20, ground);
        blocks.add(0.05, new BlockInfo().add(new ResourceLocation("huntingdim", "frame:0")));
        blocks.add(0.1, new BlockInfo().add(Blocks.MOB_SPAWNER));
        blocks.add(0.05, new BlockInfo().add(Blocks.WATER));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("zombie"));
        mob.add(5, new ResourceLocation("skeleton"));
        mob.add(4, new ResourceLocation("spider"));
        mob.add(4, new ResourceLocation("cave_spider"));
        mob.add(1, new ResourceLocation("creeper"));
        mob.add(0.2, new ResourceLocation("witch"));
    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(2, new BlockInfo().add(new ResourceLocation("goodnightsleep", "white_log")))
                .add(2, new BlockInfo().add(new ResourceLocation("goodnightsleep", "dream_log")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("goodnightsleep", "diamond_leaves")))
                .add(1.5, new BlockInfo().add(new ResourceLocation("goodnightsleep", "candy_leaves")))
                .add(1.5, new BlockInfo().add(new ResourceLocation("goodnightsleep", "dream_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("goodnightsleep", "white_plank")))
                .add(1, new BlockInfo().add(new ResourceLocation("goodnightsleep", "dream_plank")));
    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(12, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)))
                .add(8, new BlockInfo().add(Blocks.GRAVEL))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE)))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE)))
                .add(4, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE)))

                .add(5, new BlockInfo().add("stoneBasalt"))
                .add(5, new BlockInfo().add("stoneMarble"))
                .add(5, new BlockInfo().add("stoneLimestone"));
    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(4, new BlockInfo().add(new ResourceLocation("goodnightsleep", "dream_grass"))
                    .addAt(EnumFacing.UP, Blocks.AIR, 0.25)
                    .addAt(EnumFacing.UP, new ResourceLocation("botania", "flower")))
                .add(1.5, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                    .addAt(EnumFacing.UP, Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK)))
                .add(4, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                        .addAt(new BlockPos(0, 2, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER))
                        .addAt(new BlockPos(0, 1, 0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER)))
                .add(10, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                        .addAt(EnumFacing.UP, Blocks.YELLOW_FLOWER, 1)
                        .addAt(EnumFacing.UP, Blocks.RED_FLOWER, 8)
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 20))
                .add(3, new BlockInfo().add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)));
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollectionJson<>(BlockInfo.class)
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

}
