package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsOres extends Defaults {

    private final boolean onlyBlocks;
    public DefaultsOres(boolean onlyBlocks) {
        this.onlyBlocks = onlyBlocks;
    }

    @Override
    public boolean onlyOverwrite() {
        return false;
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(oreBlocks().size() * 0.2, oreBlocks());

        if(onlyBlocks)
            return;

        blocks.add(DefaultsOverworld.ores().size(), DefaultsOverworld.ores());
        blocks.add(DefaultsNether.ores().size(), DefaultsNether.ores());
        blocks.add(DefaultsEnd.ores().size(), DefaultsEnd.ores());

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


    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.EMERALD_BLOCK))
                .add(1, new BlockInfo().add(Blocks.DIAMOND_BLOCK))
                .add(1, new BlockInfo().add(Blocks.REDSTONE_BLOCK))
                .add(1, new BlockInfo().add(Blocks.LAPIS_BLOCK))
                .add(1, new BlockInfo().add(Blocks.GOLD_BLOCK))
                .add(1, new BlockInfo().add(Blocks.IRON_BLOCK))
                .add(1, new BlockInfo().add(Blocks.COAL_BLOCK))
                .add(1, new BlockInfo().add(Blocks.QUARTZ_BLOCK.getDefaultState()))

                .add(1, new BlockInfo().add("blockLead"))
                .add(1, new BlockInfo().add("blockTin"))
                .add(1, new BlockInfo().add("blockCopper"))
                .add(1, new BlockInfo().add("blockNickel"))
                .add(1, new BlockInfo().add("blockAluminum"))
                .add(1, new BlockInfo().add("blockSilver"))
                .add(1, new BlockInfo().add("blockPlatinum"))
                .add(1, new BlockInfo().add("blockIridium"))
                .add(1, new BlockInfo().add("blockSteel"))
                .add(1, new BlockInfo().add("blockElectrum"))
                .add(1, new BlockInfo().add("blockInvar"))
                .add(1, new BlockInfo().add("blockBronze"))
                .add(1, new BlockInfo().add("blockConstantan"))
                .add(1, new BlockInfo().add("blockSignalum"))
                .add(1, new BlockInfo().add("blockLumium"))
                .add(1, new BlockInfo().add("blockEnderium"))

                .add(5, new BlockInfo().add(new ResourceLocation("twilightforest", "block_storage")))

                .add(0.5, new BlockInfo().add("blockRuby"))
                .add(0.5, new BlockInfo().add("blockPeridot"))
                .add(0.5, new BlockInfo().add("blockTopaz"))
                .add(0.5, new BlockInfo().add("blockTanzanite"))
                .add(0.5, new BlockInfo().add("blockMalachite"))
                .add(0.5, new BlockInfo().add("blockSapphire"))
                .add(3, new BlockInfo().add("blockAmber"))

                .add(2, new BlockInfo().add("blockBrass"))
                .add(2, new BlockInfo().add("blockThaumium"))
                .add(1, new BlockInfo().add("blockVoid"))

                .add(1.5, new BlockInfo().add(new ResourceLocation("botania", "storage:0")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("botania", "storage:1")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("botania", "storage:2")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("botania", "storage:3")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("botania", "storage:4")))

                .add(1, new BlockInfo().add(new ResourceLocation("biomesoplenty", "gem_block:0")))
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "biotite_block:0")));
    }

}
