package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTableList;

public class DefaultsTropics extends Defaults {

    @Override
    public IBlockState getFillState(int floor) {
        if(floor == floors()[1])
            return super.getFillState(floor);

        ResourceLocation water = new ResourceLocation("tropicraft", "water");
        return Block.REGISTRY.containsKey(water) ? Block.REGISTRY.getObject(water).getDefaultState() : super.getFillState(floor);
    }

    @Override
    public boolean onlyOverwrite() {
        return false;
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> ground = new RandomCollection<BlockInfo>()
                .add(1, DefaultsOverworld.rock())
                .add(1, grass())
                .add(0.5, sand());

        RandomCollection<BlockInfo> trees = new RandomCollection<BlockInfo>()
                .add(2, logs())
                .add(1, planks())
                .add(2, leaves());

        if(floor == floors()[0]) {

            blocks.add(5, ground);
            blocks.add(15, oceanFloor());

        } else {

            blocks.add(2, trees);
            blocks.add(10, ground);
            blocks.add(1, ores());
            blocks.add(0.5, new BlockInfo().add(Blocks.MOB_SPAWNER));

        }

    }

    @Override
    public int[] floors() {
        return new int[] {0, getHeight()/2};
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

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.EMERALD_ORE))
                .add(2, new BlockInfo().add(Blocks.DIAMOND_ORE))
                .add(8, new BlockInfo().add(Blocks.REDSTONE_ORE))
                .add(6, new BlockInfo().add(Blocks.LAPIS_ORE))
                .add(3, new BlockInfo().add(Blocks.GOLD_ORE))
                .add(10, new BlockInfo().add(Blocks.IRON_ORE))
                .add(20, new BlockInfo().add(Blocks.COAL_ORE))

                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "ore:0")))
                .add(5, new BlockInfo().add(new ResourceLocation("tropicraft", "ore:1")))
                .add(12, new BlockInfo().add(new ResourceLocation("tropicraft", "ore:2")))

                .add(6, new BlockInfo().add("oreAluminum"))
                .add(5, new BlockInfo().add("oreSilver"))
                .add(5, new BlockInfo().add("oreLead"))

                .add(0.8, new BlockInfo().add("oreTopaz"))
                .add(0.8, new BlockInfo().add("oreTanzanite"))
                .add(0.8, new BlockInfo().add("oreSapphire"));
    }

    public static RandomCollection<BlockInfo> oceanFloor() {
        return new RandomCollection<BlockInfo>()
                .add(8, new BlockInfo().add(new ResourceLocation("tropicraft", "sand")))
                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "sand"))
                    .addAt(EnumFacing.UP, new ResourceLocation("tropicraft", "coral")))
                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "ore:4")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("tropicraft", "ore:3")));
    }

    public static RandomCollection<BlockInfo> sand() {
        return new RandomCollection<BlockInfo>()
                .add(10, new BlockInfo().add(new ResourceLocation("tropicraft", "sand")));
    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollection<BlockInfo>()
                .add(10, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                        .addAt(EnumFacing.UP, Blocks.AIR, 3)
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)))
                .add(10, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                        .addAt(EnumFacing.UP, new ResourceLocation("tropicraft", "flower")))
                .add(1, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                    .addAt(EnumFacing.UP, new ResourceLocation("tropicraft", "flower")))
                .add(0.1, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                    .addAt(EnumFacing.UP, new ResourceLocation("tropicraft", "bamboo_shoot")))
                .add(0.5, new BlockInfo().add(Blocks.GRASS.getDefaultState())
                    .addAt(new BlockPos(0,1,0), new ResourceLocation("tropicraft", "pineapple:0"))
                    .addAt(new BlockPos(0,2,0), new ResourceLocation("tropicraft", "pineapple:8")));
    }

    public static RandomCollection<BlockInfo> logs() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK)))
                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "log")));
    }

    public static RandomCollection<BlockInfo> leaves() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "leaves")))
                .add(2, new BlockInfo().add(new ResourceLocation("tropicraft", "leaves_fruit")));
    }

    public static RandomCollection<BlockInfo> planks() {
        return new RandomCollection<BlockInfo>()
                .add(1, new BlockInfo().add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)))
                .add(1, new BlockInfo().add(new ResourceLocation("tropicraft", "plank")));
    }

}
