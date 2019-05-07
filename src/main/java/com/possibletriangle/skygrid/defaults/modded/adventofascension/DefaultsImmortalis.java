package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class DefaultsImmortalis extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_squares")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_rectangles")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_stream_vertical")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_stream_horizontal")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_tiles")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_dirt")))
        );

        blocks.add(0.1, new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_glass")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "archaic_light")))
        );

        blocks.add(0.03, new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "gold_accumulator")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "pure_gold_accumulator")))
        );

        blocks.add(0.1, new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.LAVA))
        );

        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));


    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {

    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(0.2, new ResourceLocation("aoa3", "dungeon_keeper"));
        mob.add(1, new ResourceLocation("aoa3", "shavo"));
        mob.add(2, new ResourceLocation("aoa3", "fenix"));
        mob.add(1, new ResourceLocation("aoa3", "skelekyte"));
        mob.add(1, new ResourceLocation("aoa3", "skeledon"));
        mob.add(1, new ResourceLocation("aoa3", "goldum"));
        mob.add(1, new ResourceLocation("aoa3", "goldus"));
        mob.add(1, new ResourceLocation("aoa3", "reaver"));
        mob.add(1, new ResourceLocation("aoa3", "urioh"));
        mob.add(1, new ResourceLocation("aoa3", "ghastus"));
        mob.add(1, new ResourceLocation("aoa3", "urv"));
        mob.add(1, new ResourceLocation("aoa3", "visage"));
    }

}
