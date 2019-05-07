package com.possibletriangle.skygrid.defaults.modded.adventofascension;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsCandyland extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        blocks.add(1, candy());
        blocks.add(1, grass());
        blocks.add(0.07, new BlockInfo().add(Blocks.WATER));

    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "red_lollypop"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "blue_lollypop"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "yellow_lollypop")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "red_lollypop"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "blue_lollypop"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "yellow_lollypop"))
                    .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "red_lollypop"))
                    .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "blue_lollypop"))
                    .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "yellow_lollypop")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass"))
                    .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aoa3", "plastic_stick"))
                    .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aoa3", "candy_tube")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "red_peppermint"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "blue_peppermint"))
                    .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "yellow_peppermint")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aoa3", "candyland_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "candy_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "blue_candy_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aoa3", "candy_cane")));
    }

    public static RandomCollection<BlockInfo> candy() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(2, new BlockInfo().add(new ResourceLocation("aoa3", "plastic")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "red_candy")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "white_candy")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "aqua_cotton_candy")))
                .add(1, new BlockInfo().add(new ResourceLocation("aoa3", "pink_cotton_candy")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aoa3", "gingerbread")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aoa3", "gingerbread_man_spawner")))
                .add(0.7, new BlockInfo().add(new ResourceLocation("aoa3", "chocolate_block")))
                .add(0.7, new BlockInfo().add(new ResourceLocation("aoa3", "dark_chocolate_block")))
                .add(0.7, new BlockInfo().add(new ResourceLocation("aoa3", "white_chocolate_block")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
