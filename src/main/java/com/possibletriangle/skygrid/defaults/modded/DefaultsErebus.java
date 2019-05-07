package com.possibletriangle.skygrid.defaults.modded;

import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.generation.WorldProviderSkygridErebus;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.RandomCollectionJson;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DefaultsErebus extends Defaults {

    @Override
    public Class providerClass(boolean overwriting) {
        return WorldProviderSkygridErebus.class;
    }

    @Override
    public void registerBlocks(RandomCollection<BlockInfo> blocks, int floor) {

        RandomCollection<BlockInfo> fluids = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.WATER));

        RandomCollection<BlockInfo> container = new RandomCollectionJson<>(BlockInfo.class)
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "giant_lily_pad")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "wasp_nest")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "umber_furnace")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("erebus", "petrified_wood_chest")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "bamboo_crate")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "petrified_crafting_table")));

        RandomCollection<BlockInfo> building = new RandomCollectionJson<>(BlockInfo.class)
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "honey_comb")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("erebus", "mud_brick")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("erebus", "temple_brick")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "mir_brick")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "wither_web")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "amber_bricks")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "amber_glass")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "red_gem:2")))
                .add(0.2, new BlockInfo().add("stoneUmber").addAt(EnumFacing.UP, new ResourceLocation("erebus", "glowing_jar")));

        BlockInfo giant_flower = new BlockInfo()
                .add(new ResourceLocation("erebus", "giant_flower_stigma"))
                .add(new ResourceLocation("erebus", "giant_flower"))
                .add(new ResourceLocation("erebus", "giant_flower:15"), 0.4);

        BlockInfo pillar = new BlockInfo()
                .add(new ResourceLocation("erebus", "temple_tile"))
                .addAt(new BlockPos(0, 1, 0), new ResourceLocation("erebus", "temple_pillar"))
                .addAt(new BlockPos(0, 2, 0), new ResourceLocation("erebus", "temple_pillar"))
                .addAt(new BlockPos(0, 3, 0), new ResourceLocation("erebus", "temple_pillar"))
                .addAt(new BlockPos(0, 4, 0), new ResourceLocation("erebus", "temple_tile"));

        RandomCollection<BlockInfo> bridge = new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo()
                    .add(new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(-3, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(-2, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(-1, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(1, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(2, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4"))
                    .addAt(new BlockPos(3, 0, 0), new ResourceLocation("erebus", "bamboo_bridge:4")))
                .add(1, new BlockInfo()
                    .add(new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, -3), new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, -2), new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, -1), new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, 1), new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, 2), new ResourceLocation("erebus", "bamboo_bridge:2"))
                    .addAt(new BlockPos(0, 0, 3), new ResourceLocation("erebus", "bamboo_bridge:2")));

        blocks.add(0.08, new BlockInfo().add(new ResourceLocation("erebus", "red_gem:0")));
        blocks.add(1, fluids);
        blocks.add(0.25, bridge);
        blocks.add(0.15, pillar);
        blocks.add(0.1, container);
        blocks.add(10, rock());
        blocks.add(5, grass());
        blocks.add(5, plants());
        blocks.add(0.5, giant_flower);
        blocks.add(1, mushrooms());
        blocks.add(4, trees());
        blocks.add(2, ores());
        blocks.add(0.1, oreBlocks());
        blocks.add(2, building);

    }

    public static RandomCollection<BlockInfo> plants() {
        return  new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(Blocks.DIRT.getDefaultState())
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:3"))
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:4")))
                .add(3, new BlockInfo().add(Blocks.GRASS)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "log_bamboo"))
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "jade_berry_bush"), 0.5)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "heart_berry_bush"), 0.5)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "swamp_berry_bush"), 0.5)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:0")))
                .add(1, new BlockInfo().add(Blocks.SAND)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "prickly_pear")))
                .add(1, new BlockInfo().add(Blocks.SAND).add(new ResourceLocation("erebus", "mud"))
                        .addAt(new BlockPos(0,1,0), new ResourceLocation("erebus", "double_plant:0"))
                        .addAt(new BlockPos(0,2,0), new ResourceLocation("erebus", "double_plant:8")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "mud"))
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:2")))
                .add(1, new BlockInfo().add("stoneUmber")
                    .addAt(EnumFacing.DOWN, new ResourceLocation("erebus", "dark_fruit_vine")));
    }

    public static RandomCollection<BlockInfo> trees() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(20, new BlockInfo().add(new ResourceLocation("erebus", "planks")))
                .add(1, new BlockInfo().add("plankPetrified"))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_baobab")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_scorched")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_mahogany")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_mossbark")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_asper")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_cypress")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_balsam")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_rotten")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_marshwood")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "log_eucalyptus")));

    }

    public static RandomCollection<BlockInfo> mushrooms() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "sarcastic_czech_mushroom_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "grandmas_shoes_mushroom_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "dutch_cap_mushroom_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "kaizers_fingers_mushroom_block")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "dark_capped_mushroom_block")));

    }

    public static RandomCollection<BlockInfo> grass() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(2, new BlockInfo().add(Blocks.DIRT.getDefaultState()))
                .add(2, new BlockInfo().add(Blocks.GRASS))
                .add(2, new BlockInfo().add(Blocks.GRASS)
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:5"))
                        .addAt(EnumFacing.UP, new ResourceLocation("erebus", "small_plant:6"))
                        .addAt(EnumFacing.UP, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)))
                .add(1, new BlockInfo().add(Blocks.GRASS)
                        .addAt(new BlockPos(0,1,0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER))
                        .addAt(new BlockPos(0,2,0), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER)))
                .add(0.4, new BlockInfo().add(new ResourceLocation("erebus", "swamp_vent")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "ghost_sand")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "quick_sand")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "dung")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "mud")));
    }

    public static RandomCollection<BlockInfo> rock() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(1, new BlockInfo().add("stoneUmber"))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:1")))
                .add(0.3, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:2")))
                .add(0.08, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:3")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:4")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:5")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "umberstone:6")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "umbergravel")))
                .add(0.2, new BlockInfo().add(new ResourceLocation("erebus", "umberstone_pillar")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("erebus", "volcanic_rock")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "gneiss:0")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "gneiss")))
                .add(0.1, new BlockInfo().add(new ResourceLocation("erebus", "dust")));
    }

    public static RandomCollection<BlockInfo> ores() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(5, new BlockInfo().add(new ResourceLocation("erebus", "ore_iron")))
                .add(3, new BlockInfo().add(new ResourceLocation("erebus", "ore_gold")))
                .add(10, new BlockInfo().add(new ResourceLocation("erebus", "ore_coal")))
                .add(3, new BlockInfo().add(new ResourceLocation("erebus", "ore_silver")))
                .add(1.2, new BlockInfo().add(new ResourceLocation("erebus", "ore_diamond")))
                .add(0.8, new BlockInfo().add(new ResourceLocation("erebus", "ore_emerald")))
                .add(2, new BlockInfo().add(new ResourceLocation("erebus", "ore_lapis")))
                .add(7, new BlockInfo().add(new ResourceLocation("erebus", "ore_quartz")))

                .add(6, new BlockInfo().add(new ResourceLocation("erebus", "ore_copper")))
                .add(4, new BlockInfo().add(new ResourceLocation("erebus", "ore_tin")))
                .add(3, new BlockInfo().add(new ResourceLocation("erebus", "ore_lead")))
                .add(4, new BlockInfo().add(new ResourceLocation("erebus", "ore_aluminium")))
                .add(4, new BlockInfo().add(new ResourceLocation("erebus", "ore_jade")))
                .add(3, new BlockInfo().add(new ResourceLocation("erebus", "ore_gneiss")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "ore_fossil")))
                .add(3, new BlockInfo().add(new ResourceLocation("erebus", "ore_petrified_wood")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "ore_temple")))
                .add(0.5, new BlockInfo().add(new ResourceLocation("erebus", "ore_encrusted_diamond")));
    }

    public static RandomCollection<BlockInfo> oreBlocks() {
        return new RandomCollectionJson<>(BlockInfo.class)
                .add(3, new BlockInfo().add("blockJade"))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "silk")))
                .add(1, new BlockInfo().add(new ResourceLocation("erebus", "rein_exo")));
    }

    @Override
    public void registerLoot(RandomCollection<ResourceLocation> tables) {
    }

    @Override
    public void registerMobs(RandomCollection<ResourceLocation> mob) {
    }

}
