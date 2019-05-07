package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsAbyss extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, grass());
        blocks.add(1, rock());
        blocks.add(0.5, leaves());
        blocks.add(0.5, wood());

        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "abyss_stone")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "toxic_block")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "bloodstone_bars")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "bloodstone_bricks")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "bloodstone_bar_bricks")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "bloodstone_ore")));
    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "abyss_grass")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "abyss_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "tubeicles")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "abyss_grass"))
                        .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "eye_shrub_stem"))
                        .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "eye_shrub")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "abyss_grass"))
                        .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "blood_pine_stem"))
                        .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "blood_pine")));
    }

    public static RandomCollection<BlockInfo> leaves() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "shadow_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "shadowblood_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "blood_leaves")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "blood_leaves"))
                    .addAt(new BlockPos(0, -1, 0), new ResourceLocation("aoa3", "blood_strands"))
                    .addAt(new BlockPos(0, -2, 0), new ResourceLocation("aoa3", "eye_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "vein_leaves")));
    }

    public static RandomCollection<BlockInfo> wood() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "shadow_log")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "blood_log")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "eyeball_log")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "shadow_planks")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "bloodwood_planks")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "tentacles")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "tentacles_eye_red")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aoa3", "tentacles_eye_orange")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {

    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(0.1, new ResourceLocation("aoa3", "abyssal_lottoman"));
        mob.add(0.1, new ResourceLocation("aoa3", "web_reaper"));
        mob.add(1, new ResourceLocation("aoa3", "occulent"));
        mob.add(1, new ResourceLocation("aoa3", "flesh_eater"));
        mob.add(1, new ResourceLocation("aoa3", "fiend"));
        mob.add(1, new ResourceLocation("aoa3", "apparition"));
        mob.add(1, new ResourceLocation("aoa3", "distorter"));
    }

}
