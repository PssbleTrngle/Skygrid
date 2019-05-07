package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class DefaultsPrecasia extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, ground());
        blocks.add(2, trees());
        blocks.add(0.3, bricks());
        blocks.add(0.05, spawers());
        blocks.add(0.07, new BlockInfo().add(Blocks.LAVA));

    }

    public static RandomCollection<BlockInfo> ground() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "chestbone_fragments_ore")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "footbone_fragments_ore")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "skullbone_fragments_ore")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "legbone_fragments_ore")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "low_precasia_stone")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "high_precasia_stone")))
                .add(2, new BlockInfo().add(new ResourceLocation("aoa3", "precasia_grass")))
                .add(2, new BlockInfo().add(new ResourceLocation("aoa3", "precasia_grass"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "giant_snail_acid"), 0.02)
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "lucon_grass"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "tangle_thorns")));
    }

    public static RandomCollection<BlockInfo> bricks() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(3, new BlockInfo().add(new ResourceLocation("aoa3", "dark_bricks")))
                .add(0.6, new BlockInfo().add(new ResourceLocation("aoa3", "rune_post_life")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "skeletal_bricks")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_temple_block_plain")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_temple_block_pass")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_temple_block_squares")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_temple_block_face")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_temple_block_track")));
    }

    public static RandomCollection<BlockInfo> spawers() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "spinoledon_spawner")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "diocus_spawner")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "isosaur_spawner")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "kaiyu_spawner")));
    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "cycade_log")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "cycade_planks")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "cycade_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "lucalus_log")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "lucalus_planks")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "lucalus_leaves")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "stranglewood_log")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "stranglewood_planks")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "stranglewood_leaves")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
