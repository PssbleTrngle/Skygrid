package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class DefaultsIromine extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, ground());
        blocks.add(0.6, trees());

        blocks.add(0.15, new RandomCollectionJson<>(BlockInfo.class)
                .add(0.8, new BlockInfo().add(new ResourceLocation("aoa3", "iro_glass")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "iro_lamp_off")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "iro_box")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "iro_crate")))
                .add(2, new BlockInfo().add(new ResourceLocation("aoa3", "iro_brick_trap")))
                .add(2, new BlockInfo().add(new ResourceLocation("aoa3", "iro_striped_bricks")))
        );

        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    public static RandomCollection<BlockInfo> ground() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "iromine_stone")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "iromine_grass")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "iromine_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "irotops"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "iro_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "iro_grass")));
    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "iro_log")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "irodust_leaves")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "irogold_leaves")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {

    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(1, new ResourceLocation("aoa3", "quickpocket"));
        mob.add(1, new ResourceLocation("aoa3", "voltron"));
        mob.add(0.2, new ResourceLocation("aoa3", "polytom"));
        mob.add(0.1, new ResourceLocation("aoa3", "foraging_master"));
        mob.add(0.1, new ResourceLocation("aoa3", "mechamaton"));
        mob.add(0.02, new ResourceLocation("aoa3", "mechachron"));
        mob.add(0.1, new ResourceLocation("aoa3", "mecha_cyclops"));
        mob.add(0.1, new ResourceLocation("aoa3", "mecha_skellox"));
        mob.add(1, new ResourceLocation("aoa3", "mechyon"));
    }

}
