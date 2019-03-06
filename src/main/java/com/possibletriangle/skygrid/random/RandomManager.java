package com.possibletriangle.skygrid.random;

import com.possibletriangle.skygrid.ConfigOptions;
import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.defaults.Defaults;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class RandomManager {

    private static final HashMap<ResourceLocation, HashMap<Integer, RandomCollection<BlockInfo>>> BLOCKS = new HashMap<>();
    private static final HashMap<ResourceLocation, BlockPos> OFFSET = new HashMap<>();
    private static final HashMap<ResourceLocation, HashMap<Integer, IBlockState>> FILL_BLOCK = new HashMap<>();
    private static final HashMap<ResourceLocation, Integer> HEIGHT = new HashMap<>();
    private static final HashMap<ResourceLocation, RandomCollection<ResourceLocation>> MOBS = new HashMap<>();
    private static final HashMap<ResourceLocation, RandomCollection<ResourceLocation>> LOOT = new HashMap<>();

    private static RandomCollection<BlockInfo> forDimension(ResourceLocation dimensionID, int atY) {
        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        Defaults defaults = GameRegistry.findRegistry(Defaults.class).getValue(dimensionID);
        if(defaults == null) {
            Skygrid.LOGGER.error("Could not find default for \"{}\"", dimensionID);
            return null;
        }

        if(!BLOCKS.containsKey(dimensionID)) {
            Skygrid.LOGGER.info("Registered generation for dimemsion {}", dimensionID);
            HashMap<Integer, RandomCollection<BlockInfo>> map = new HashMap<>();
            for(int floor : defaults.floors())
                map.put(floor, new RandomCollection<>());
            BLOCKS.put(dimensionID, map);
        }

        for(int i = defaults.floors().length-1; i>=0; i--)
            if(defaults.floors()[i] <= atY)
                return BLOCKS.get(dimensionID).get(defaults.floors()[i]);

        return null;
    }

    public static BlockPos getOffset(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        BlockPos offset = OFFSET.get(dimensionID);

        if(offset == null) {
            Skygrid.LOGGER.error("No offset defined for dimension \"{}\"", dimensionID);
            offset = new BlockPos(4, 4, 4);
        }

        return offset;
    }

    public static int getHeight(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return Math.max(HEIGHT.get(dimensionID), 4);
    }

    public static RandomCollection<ResourceLocation> getLoot(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        if(!LOOT.containsKey(dimensionID))
            LOOT.put(dimensionID, new RandomCollection<>());

        return LOOT.get(dimensionID);
    }

    public static RandomCollection<ResourceLocation> getMobs(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        if(!MOBS.containsKey(dimensionID))
            MOBS.put(dimensionID, new RandomCollection<>());

        return MOBS.get(dimensionID);
    }

    public static IBlockState getFillBlock(ResourceLocation dimensionID, int atY) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        IBlockState fillBlock = null;
        for(int floor : FILL_BLOCK.get(dimensionID).keySet())
            if(floor <= atY) {
                fillBlock = FILL_BLOCK.get(dimensionID).get(floor);
            }

        if(fillBlock == null) {
            Skygrid.LOGGER.error("No fill block defined for dimension \"{}\"", dimensionID);
            fillBlock = Blocks.AIR.getDefaultState();
        }
        return fillBlock;
    }

    public static BlockInfo next(ResourceLocation dimensionType, Random random, int atY) {
        BlockInfo info = forDimension(dimensionType, atY).next(random);
        if(info == null)
            throw new NullPointerException("No blocks defined for \"" + dimensionType.toString() + "\"");

        return info;
    }

    public static void reload() {

        IForgeRegistry<Defaults> REGISTRY = GameRegistry.findRegistry(Defaults.class);

        for(Defaults defaults : REGISTRY) {

            ResourceLocation name = REGISTRY.getKey(defaults);
            if(name == null || Arrays.asList(ConfigOptions.BLACKLISTED).contains(name.getResourcePath()))
                continue;

            FILL_BLOCK.put(name, new HashMap<>());
            for(int floor : defaults.floors()) {
                RandomCollection<BlockInfo> blocks = forDimension(name, floor);
                blocks.clear();
                defaults.registerBlocks(blocks, floor);
                FILL_BLOCK.get(name).put(floor, defaults.getFillState(floor));
            }

            OFFSET.put(name, new BlockPos(defaults.getOffset(EnumFacing.Axis.X), defaults.getOffset(EnumFacing.Axis.Y), defaults.getOffset(EnumFacing.Axis.Z)));
            HEIGHT.put(name, defaults.getHeight());

            getLoot(name).clear();
            getMobs(name).clear();
            defaults.registerLoot(getLoot(name));
            defaults.registerMobs(getMobs(name));

        }

    }

    public static ResourceLocation[] dimensions() {
        return BLOCKS.keySet().toArray(new ResourceLocation[0]);
    }

}
