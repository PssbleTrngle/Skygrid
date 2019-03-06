package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsLimbo extends Defaults {

    @Override
    public int getHeight() {
        return 10000;
    }

    @Override
    public int getOffset(EnumFacing.Axis axis) {
        return 8;
    }

    @Override
    public boolean onlyOverwrite() {
        return false;
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, new BlockInfo().add(Blocks.BEDROCK));

    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
