package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class DefaultsTwilight extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> fluids = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.WATER));

        RandomCollection<BlockInfo> misc = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "firefly_jar")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("twilightforest", "uncrafting_table")));

        RandomCollection<BlockInfo> building = new RandomCollectionJson<>(BlockInfo.class)
                .add(0.2, new RandomCollectionJson<>(BlockInfo.class)
                        .add(0.1, new BlockInfo().add(new ResourceLocation("twilightforest", "castle_pillar")))
                        .add(0.1, new BlockInfo().add(new ResourceLocation("twilightforest", "castle_brick")))
                        .add(0.2, new BlockInfo().add(new ResourceLocation("twilightforest", "castle_rune_brick"))))
                .add(1, new RandomCollectionJson<>(BlockInfo.class)
                        .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "naga_stone")))
                        .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "etched_nagastone")))
                        .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "etched_nagastone_mossy")))
                        .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "nagastone_pillar")))
                        .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "nagastone_pillar_mossy"))))
                .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "maze_stone")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("twilightforest", "spiral_bricks")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("twilightforest", "underbrick")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("twilightforest", "tower_wood")))
                .add(0.4, new BlockInfo().add(new ResourceLocation("twilightforest", "trollsteinn")))
                .add( 0.1, new RandomCollectionJson<>(BlockInfo.class)
                        .add(5, new BlockInfo().add(new ResourceLocation("twilightforest", "aurora_block")))
                        .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "auroralized_glass")))
                        .add(5, new BlockInfo().add(new ResourceLocation("twilightforest", "aurora_pillar"))));

        RandomCollection<BlockInfo> ground = new RandomCollectionJson<>(BlockInfo.class)
                .add(3, DefaultsOverworld.rock())
                .add(1, DefaultsOverworld.dirt())
                .add(4, grass())
                .add(0.5, new BlockInfo().add(Blocks.CLAY))
                .add(0.1, new BlockInfo().add(Blocks.SNOW))
                .add(0.1, new BlockInfo().add(Blocks.ICE));

        RandomCollection<BlockInfo> tree = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, logs())
                .add(0.5, planks())
                .add(1, leaves())
                .add(0.2, new BlockInfo().add(new ResourceLocation("twilightforest", "root:0")))
                .add(0.02, new BlockInfo().add(new ResourceLocation("twilightforest", "root:1")));

        blocks.add(1, misc);
        blocks.add(5, building);
        blocks.add(20, tree);
        blocks.add(20, ground);
        blocks.add(1, fluids);
        blocks.add(2, ores());
        blocks.add(0.15, oreBlocks());

    }

    public static RandomCollection<BlockInfo> leaves() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.1, new BlockInfo().add(new ResourceLocation("twilightforest", "magic_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "hedge")))
                .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "dark_leaves")))
                .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "twilight_leaves:0")))
                .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "twilight_leaves:1")))
                .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "twilight_leaves:2")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("twilightforest", "twilight_leaves:3")))
                .add(2, new BlockInfo().add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)))
                .add(1, new BlockInfo().add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)));
    }

    public static RandomCollection<BlockInfo> logs() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.1, new BlockInfo().add(new ResourceLocation("twilightforest", "thorns")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("twilightforest", "magic_log")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("twilightforest", "magic_log_core")))
                .add(14, new BlockInfo().add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK), 2)
                .add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH), 1)
                .add(new ResourceLocation("twilightforest", "twilight_log"), 8)
                .addAt(EnumFacing.NORTH, Blocks.AIR.getDefaultState(), 3)
                .addAt(EnumFacing.NORTH, new ResourceLocation("twilightforest", "firefly:2"))
                .addAt(EnumFacing.NORTH, new ResourceLocation("twilightforest", "cicada:2")));
    }

    public static RandomCollection<BlockInfo> planks() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(2, new BlockInfo().add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)))
                .add(2, new BlockInfo().add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)))
                .add(1, new BlockInfo().add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)));
    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, DefaultsOverworld.grass())
                .add(0.5, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:0"))
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:1"))
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:2"))
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:3"))
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:4"))
                    .addAt(EnumFacing.UP, new ResourceLocation("twilightforest", "twilight_plant:5"))

                    .addAt(EnumFacing.DOWN, new ResourceLocation("twilightforest", "twilight_plant:7"))
                    .addAt(EnumFacing.DOWN, new ResourceLocation("twilightforest", "twilight_plant:8")));
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

                .add(0.8, new BlockInfo().add("oreTopaz"))
                .add(0.8, new BlockInfo().add("oreTanzanite"))
                .add(0.8, new BlockInfo().add("oreMalachite"))
                .add(4, new BlockInfo().add("oreAmber"))
                .add(4, new BlockInfo().add("oreCinnabar"));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollectionJson<>(BlockInfo.class)
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

                .add(2, new BlockInfo().add(new ResourceLocation("twilightforest", "block_storage:0")))
                .add(1, new BlockInfo().add("blockFiery"))
                .add(2, new BlockInfo().add("blockSteeleaf"))
                .add(1, new BlockInfo().add(new ResourceLocation("twilightforest", "block_storage:3")))
                .add(0.75, new BlockInfo().add(new ResourceLocation("twilightforest", "block_storage:4")))

                .add(0.8, new BlockInfo().add("blockTopaz"))
                .add(0.8, new BlockInfo().add("blockTanzanite"))
                .add(0.8, new BlockInfo().add("blockMalachite"))
                .add(1, new BlockInfo().add("blockAmber"));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
