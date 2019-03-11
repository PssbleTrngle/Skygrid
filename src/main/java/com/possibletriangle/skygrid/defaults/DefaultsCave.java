package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsCave extends Defaults {

    @Override
    public boolean onlyOverwrite() {
        return false;
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(10, new RandomCollectionJson<>(BlockInfo.class)
                .add(1, DefaultsOverworld.ores())
                .add(1, DefaultsTwilight.ores()));
        blocks.add(30, DefaultsOverworld.rock());
        blocks.add(0.2, new BlockInfo().add(new ResourceLocation("quark", "crystal")));
        blocks.add(0.5, new BlockInfo().add(Blocks.MOB_SPAWNER));

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

}
