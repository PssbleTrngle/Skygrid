package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DefaultsTeletory extends Defaults {

    @Override
    public int getHeight() {
        return Math.min(getOffset(EnumFacing.Axis.Y) * 8, super.getHeight());
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, new BlockInfo().add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT)));
        blocks.add(0.1, new BlockInfo().add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT))
            .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH)));
        blocks.add(2, new BlockInfo().add(Blocks.END_STONE));
        blocks.add(0.07, new BlockInfo().add(new ResourceLocation("teletoro", "enderore")));
        blocks.add(0.01, new BlockInfo().add(new ResourceLocation("teletoro", "enderblock")));

    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("enderman"));
        mob.add(8, new ResourceLocation("endermite"));
        mob.add(2, new ResourceLocation("shulker"));
    }

}
