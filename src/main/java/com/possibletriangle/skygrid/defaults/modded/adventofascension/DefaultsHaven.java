package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class DefaultsHaven extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, ground());
        blocks.add(0.5, trees());

        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    public static RandomCollection<BlockInfo> ground() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "crystallite_ore")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "haven_stone")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("aoa3", "haven_dirt")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "haven_grass")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "haven_grass"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "daylooms_blue"), 1)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "daylooms_pink"), 1)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "daylooms_yellow"), 1)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "havendales_yellow"), 0.5)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "havendales_blue"), 0.5)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "havendales_pink"), 0.5)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "haven_grass_plant"), 6));
    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(6, new BlockInfo().add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK)))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "blue_haven_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "pink_haven_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "purple_haven_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "red_haven_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "turquoise_haven_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "yellow_haven_leaves")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {

    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(1, new ResourceLocation("aoa3", "angelica"));
        mob.add(0.2, new ResourceLocation("aoa3", "blue_automaton"));
        mob.add(0.2, new ResourceLocation("aoa3", "red_automaton"));
        mob.add(0.2, new ResourceLocation("aoa3", "yellow_automaton"));
        mob.add(0.2, new ResourceLocation("aoa3", "purple_automaton"));
        mob.add(0.2, new ResourceLocation("aoa3", "green_automaton"));
        mob.add(0.5, new ResourceLocation("aoa3", "orbiter"));
    }

}
