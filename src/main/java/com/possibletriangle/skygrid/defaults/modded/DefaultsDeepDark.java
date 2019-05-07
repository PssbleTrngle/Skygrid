package com.possibletriangle.skygrid.defaults.modded;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.defaults.vanilla.DefaultsOverworld;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsDeepDark extends Defaults {

    @Override
    public int getOffset(EnumFacing.Axis axis) {
        return axis == EnumFacing.Axis.Y ? 3 : super.getOffset(axis);
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, rock());
        blocks.add(1, DefaultsOverworld.ores());
        blocks.add(1, new RandomCollectionJson<>(BlockInfo.class)
            .add(1, new BlockInfo().add(Blocks.WATER))
            .add(0.8, new BlockInfo().add(Blocks.LAVA)));

    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
        tables.add(10, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
        tables.add(10, LootTableList.CHESTS_SIMPLE_DUNGEON);
        tables.add(4, LootTableList.CHESTS_DESERT_PYRAMID);
        tables.add(4, LootTableList.CHESTS_JUNGLE_TEMPLE);
        tables.add(2, LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
        tables.add(2, LootTableList.CHESTS_STRONGHOLD_CROSSING);
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

    public RandomCollection<BlockInfo> rock() {
        return  new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)))
                .add(0.05, new BlockInfo().add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE))
                        .addAt(EnumFacing.UP, Blocks.RED_MUSHROOM)
                        .addAt(EnumFacing.UP, Blocks.BROWN_MUSHROOM, 0.2))
                .add(2, new BlockInfo().add(Blocks.COBBLESTONE));
    }

}
