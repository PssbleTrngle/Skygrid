package com.possibletriangle.skygrid.defaults.vanilla;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsOcean extends Defaults {

    @Override
    public boolean onlyOverwrite() {
        return false;
    }

    @Override
    public int getOffset(EnumFacing.Axis axis) {
        return 6;
    }

    @Override
    public IBlockState getFillState(int floor) {
        return Blocks.WATER.getDefaultState();
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> ice = new RandomCollectionJson<>(BlockInfo.class)
                .add(4, new BlockInfo().add(Blocks.ICE))
                .add(2, new BlockInfo().add(Blocks.PACKED_ICE))
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "biome_cobblestone:1")));

        RandomCollection<BlockInfo> prismarine = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.ROUGH)))
                .add(0.4, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS)))
                .add(0.2, new BlockInfo().add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK)))
                .add(0.3, new BlockInfo().add(Blocks.SEA_LANTERN));

        RandomCollection<BlockInfo> ground = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.CLAY)
                .add(Blocks.GRAVEL)
                .add(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND))
                .add(new ResourceLocation("biomesoplenty", "white_sand:0"))
                .addAt(EnumFacing.UP, getFillState(floor), 3)
                .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "coral"))
                .addAt(EnumFacing.UP, new ResourceLocation("tropicraft", "coral")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("biomesoplenty", "mud:0")));

        blocks.add(0.1, new BlockInfo().add(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true)));
        blocks.add(0.05, new BlockInfo().add(Blocks.GOLD_BLOCK));
        blocks.add(2, ground);
        blocks.add(0.3, ice);
        blocks.add(1, prismarine);
        blocks.add(0.02, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
        tables.add(1, LootTableList.CHESTS_IGLOO_CHEST);
        tables.add(1, LootTableList.CHESTS_JUNGLE_TEMPLE);
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("guardian"));
    }

}
