package com.possibletriangle.skygrid.defaults.modded;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsAetherI extends Defaults {

    @Override
    public int getOffset(EnumFacing.Axis axis) {
        switch (axis) {
            case Y:
                return super.getOffset(axis);
            default:
                return super.getOffset(axis) + 2;
        }
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> building = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "skyroot_bookshelf")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "holystone_brick")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aether_legacy", "aerogel")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("aether_legacy", "quicksoil_glass")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "dungeon_block")));

        BlockInfo pillar = new BlockInfo()
                .add(new ResourceLocation("aether_legacy", "pillar_top"))
                .addAt(new BlockPos(0, 1, 0), new ResourceLocation("aether_legacy", "pillar"))
                .addAt(new BlockPos(0, 2, 0), new ResourceLocation("aether_legacy", "pillar"))
                .addAt(new BlockPos(0, 3, 0), new ResourceLocation("aether_legacy", "pillar"))
                .addAt(new BlockPos(0, 4, 0), new ResourceLocation("aether_legacy", "pillar_top"));

        blocks.add(0.25, pillar);
        blocks.add(10, rock());
        blocks.add(10, grass());
        blocks.add(4, trees());
        blocks.add(2, ores());
        blocks.add(0.1, oreBlocks());
        blocks.add(2, building);
        blocks.add(4, new BlockInfo().add(new ResourceLocation("aether_legacy", "aercloud")));

    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.5, new BlockInfo().add(new ResourceLocation("aether_legacy", "skyroot_plank")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_log:0")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_log:1")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_leaves:0")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_leaves:1")));

    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(4, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_dirt")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("aether_legacy", "enchanted_aether_grass")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("aether_legacy", "enchanted_aether_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "golden_oak_sapling"), 1))
                .add(10, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_grass")))
                .add(10, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "berry_bush_stem"), 1)
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "berry_bush"), 0.5)
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "purple_flower"), 7)
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "white_flower"), 7))
                .add(10, new BlockInfo().add(new ResourceLocation("aether_legacy", "aether_grass"))
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "golden_oak_sapling"), 1)
                        .addAt(EnumFacing.UP, new ResourceLocation("aether_legacy", "skyroot_sapling"), 8));
    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(0.6, new BlockInfo().add(new ResourceLocation("aether_legacy", "quicksoil")))
                .add(0.05, new BlockInfo().add(new ResourceLocation("aether_legacy", "icestone")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "holystone")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("aether_legacy", "mossy_holystone")));
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(10, new BlockInfo().add(new ResourceLocation("aether_legacy", "ambrosium_ore")))
                .add(4, new BlockInfo().add(new ResourceLocation("aether_legacy", "zanite_ore")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "gravitite_ore")));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(10, new BlockInfo().add(new ResourceLocation("aether_legacy", "zanite_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("aether_legacy", "enchanted_gravitite")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
