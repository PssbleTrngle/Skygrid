package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsNether extends Defaults {

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> fluids = new RandomCollection<BlockInfo>()
                .add(6, new BlockInfo().add(Blocks.LAVA).addAtAll(FRAME_BLOCK))
                .add(0.3, new BlockInfo().add(new ResourceLocation("thermalfoundation", "fluid_glowstone")).addAtAll(FRAME_BLOCK))
                .add(0.1, new BlockInfo().add(new ResourceLocation("thermalfoundation", "fluid_pyrotheum")).addAtAll(FRAME_BLOCK))
                .add(0.6, new BlockInfo().add(new ResourceLocation("biomesoplenty", "blood")).addAtAll(FRAME_BLOCK))
                .add(0.2, new BlockInfo().add(new ResourceLocation("biomesoplenty", "honey")).addAtAll(FRAME_BLOCK));

        RandomCollection<BlockInfo> bricks = new RandomCollection<BlockInfo>()
                .add(6, new BlockInfo().add(Blocks.NETHER_BRICK))
                .add(1, new BlockInfo().add(Blocks.RED_NETHER_BRICK))
                .add(2, new BlockInfo().add(new ResourceLocation("quark", "magma_bricks")))
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "charred_nether_bricks")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("quark", "soul_sandstone:0")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("quark", "soul_sandstone:1")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("quark", "soul_sandstone:2")));

        blocks.add(3, ground());
        blocks.add(0.1, new BlockInfo().add(Blocks.LIT_PUMPKIN));
        blocks.add(0.3, new BlockInfo().add(Blocks.BONE_BLOCK));
        blocks.add(0.8, new BlockInfo().add(Blocks.GLOWSTONE));
        blocks.add(0.1, new BlockInfo().add(new ResourceLocation("quark", "blaze_lantern")));
        blocks.add(0.15, oreBlocks());
        blocks.add(1, ores());
        blocks.add(2, fluids);
        blocks.add(0.5, bricks);
        blocks.add(0.4, new BlockInfo()
                .add(new ResourceLocation("natura", "clouds:2"))
                .add(new ResourceLocation("natura", "clouds:3")));
        blocks.add(0.2, new BlockInfo()
                .add(new ResourceLocation("natura", "netherrack_furnace"))
                .add(new ResourceLocation("natura", "blaze_hopper")));
        blocks.add(0.05, new BlockInfo().add(Blocks.MOB_SPAWNER));

    }

    public static RandomCollection<BlockInfo> ground() {
            return new RandomCollection<BlockInfo>()
                .add(4, new BlockInfo().add(new ResourceLocation("biomesoplenty", "grass:6")))
                .add(8, new BlockInfo().add(new ResourceLocation("biomesoplenty", "ash_block")))
                .add(6, new BlockInfo().add(new ResourceLocation("biomesoplenty", "flesh")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("biomesoplenty", "flesh"))
                        .addAt(EnumFacing.UP, new ResourceLocation("biomesoplenty", "double_plant:2"))
                        .addAt(new BlockPos(0, 2, 0), new ResourceLocation("biomesoplenty", "double_plant:10")))
                .add(15, new BlockInfo().add(Blocks.NETHERRACK))
                .add(8, new BlockInfo().add(Blocks.NETHERRACK)
                        .addAt(EnumFacing.UP, Blocks.RED_MUSHROOM)
                        .addAt(EnumFacing.UP, Blocks.BROWN_MUSHROOM))
                .add(2, new BlockInfo().add(Blocks.MAGMA))
                .add(1, new BlockInfo().add(Blocks.GRAVEL))
                .add(0.1, new BlockInfo().add(Blocks.OBSIDIAN))
                .add(3, new BlockInfo().add(Blocks.SOUL_SAND)
                        .addAt(EnumFacing.UP, Blocks.NETHER_WART))
                .add(3, new BlockInfo().add(Blocks.SOUL_SAND))
                .add(8, new BlockInfo().add(new ResourceLocation("quark", "biome_cobblestone:0")))
                .add(0.4, new BlockInfo().add(new ResourceLocation("quark", "polished_netherrack:0")))
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "polished_netherrack:1")))
                .add(1, new BlockInfo().add(new ResourceLocation("quark", "smoker")))
                .add(1, new BlockInfo().add(new ResourceLocation("natura", "nether_heat_sand")))
                .add(1, new BlockInfo().add(new ResourceLocation("natura", "nether_tainted_soil:0")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
        tables.add(10, LootTableList.CHESTS_NETHER_BRIDGE);
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
        mob.add(10, new ResourceLocation("zombie_pigman"));
        mob.add(7, new ResourceLocation("wither_skeleton"));
        mob.add(3, new ResourceLocation("blaze"));
        mob.add(4, new ResourceLocation("magma_cube"));
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollection<BlockInfo>()
                .add(20, new BlockInfo().add(Blocks.QUARTZ_ORE))
                .add(16, new BlockInfo().add("oreNetherCoal"))
                .add(12, new BlockInfo().add("oreNetherIron"))
                .add(14, new BlockInfo().add("oreNetherRedstone"))
                .add(6, new BlockInfo().add("oreNetherCopper"))
                .add(5, new BlockInfo().add("oreNetherNickel"))
                .add(1, new BlockInfo().add("oreNetherEmerald"))
                .add(10, new BlockInfo().add("oreNetherGold"));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollection<BlockInfo>()
                .add(2, new BlockInfo().add(Blocks.COAL_BLOCK))
                .add(4, new BlockInfo().add(Blocks.REDSTONE_BLOCK))
                .add(1, new BlockInfo().add(Blocks.GOLD_BLOCK))
                .add(8, new BlockInfo().add(Blocks.NETHER_WART_BLOCK))
                .add(2, new BlockInfo().add(Blocks.QUARTZ_BLOCK.getDefaultState()))

                .add(2, new BlockInfo().add(new ResourceLocation("biomesoplenty", "honey_block")))
                .add(3, new BlockInfo().add("oreClathrateGlowstone"));
    }

}
