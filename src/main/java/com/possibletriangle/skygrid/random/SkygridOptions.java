package com.possibletriangle.skygrid.random;

import com.possibletriangle.skygrid.ConfigSkygrid;
import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.defaults.Defaults;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SkygridOptions {

    int HEIGHT;
    BlockPos OFFSET;
    final RandomCollectionRL MOBS = new RandomCollectionRL();
    final RandomCollectionRL LOOT = new RandomCollectionRL();
    final HashMap<Integer, IBlockState> FILL_BLOCK = new HashMap<>();
    final HashMap<Integer, RandomCollectionJson<BlockInfo>> BLOCKS = new HashMap<>();
    boolean onlyOverride;

    private static final HashMap<ResourceLocation, SkygridOptions> OPTIONS = new HashMap<>();

    private SkygridOptions() {
    }

    public static boolean onlyOverride(ResourceLocation name) {
        return !OPTIONS.containsKey(name) || OPTIONS.get(name).onlyOverride;
    }

    private static RandomCollection<BlockInfo> forDimension(ResourceLocation dimensionID, int atY) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return new RandomCollectionJson<>(BlockInfo.class).add(1, new BlockInfo().add(Blocks.SPONGE));
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        SkygridOptions options = OPTIONS.get(dimensionID);
        for(int i = options.BLOCKS.size()-1; i>=0; i--)
            if(options.BLOCKS.keySet().toArray(new Integer[0])[i] <= atY)
                return options.BLOCKS.get(options.BLOCKS.keySet().toArray(new Integer[0])[i]);

        return null;
    }

    public static BlockPos getOffset(ResourceLocation dimensionID) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return new BlockPos(3, 3, 3);
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        BlockPos offset = OPTIONS.get(dimensionID).OFFSET;

        if(offset == null) {
            Skygrid.LOGGER.error("No offset defined for dimension \"{}\"", dimensionID);
            offset = new BlockPos(4, 4, 4);
        }

        return offset;
    }

    public static int getHeight(ResourceLocation dimensionID) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return 100;
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return Math.max(OPTIONS.get(dimensionID).HEIGHT, 4);
    }

    public static RandomCollection<ResourceLocation> getLoot(ResourceLocation dimensionID) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return new RandomCollectionRL();
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return OPTIONS.get(dimensionID).LOOT;
    }

    public static RandomCollection<ResourceLocation> getMobs(ResourceLocation dimensionID) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return new RandomCollectionRL();
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return OPTIONS.get(dimensionID).MOBS;
    }

    public static IBlockState getFillBlock(ResourceLocation dimensionID, int atY) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return Blocks.AIR.getDefaultState();
        }

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        IBlockState fillBlock = null;
        for(int floor : OPTIONS.get(dimensionID).FILL_BLOCK.keySet())
            if(floor <= atY) {
                fillBlock = OPTIONS.get(dimensionID).FILL_BLOCK.get(floor);
            }

        if(fillBlock == null) {
            Skygrid.LOGGER.error("No fill block defined for dimension \"{}\"", dimensionID);
            fillBlock = Blocks.AIR.getDefaultState();
        }
        return fillBlock;
    }

    public static BlockInfo next(ResourceLocation dimensionID, Random random, int atY) {
        if(dimensionID == null || !OPTIONS.containsKey(dimensionID)) {
            Skygrid.LOGGER.error("Invalid dimension request for \"{}\"", dimensionID == null ? "null" : dimensionID.toString());
            return new BlockInfo().add(Blocks.SPONGE);
        }

        RandomCollection<BlockInfo> blocks = forDimension(dimensionID, atY);
        if(blocks == null) return new BlockInfo().add(Blocks.AIR);

        BlockInfo info = blocks.next(random);
        if(info == null)
            throw new NullPointerException("No blocks defined for \"" + dimensionID.toString() + "\"");

        return info;
    }

    public static void validate() {

        for(ResourceLocation name : OPTIONS.keySet()) {
            for(RandomCollectionJson<BlockInfo> blocks : OPTIONS.get(name).BLOCKS.values())
                blocks.validate();
        }

    }

    public static void reload(boolean reset) {

        IForgeRegistry<Defaults> REGISTRY = GameRegistry.findRegistry(Defaults.class);

        ArrayList<ResourceLocation> names = new ArrayList<>();
        for(Defaults defaults : REGISTRY)
            if(defaults.getRegistryName() != null)
                names.add(defaults.getRegistryName());

        for(String name : SkygridJSONConverter.getConfigs())
            if(!names.contains(new ResourceLocation(name)))
                names.add(new ResourceLocation(name));

        for(ResourceLocation name : names) {

            Defaults defaults = REGISTRY.getValue(name);
            SkygridOptions options = OPTIONS.containsKey(name) ? OPTIONS.get(name) : new SkygridOptions();

            options.BLOCKS.clear();
            options.LOOT.clear();
            options.MOBS.clear();
            options.FILL_BLOCK.clear();

            if(Arrays.asList(ConfigSkygrid.BLACKLISTED).contains(name.getResourcePath()))
                continue;

            if(!reset && SkygridJSONConverter.existsConfig(name)) {

                SkygridJSONConverter.readFromConfig(options, name);
                SkygridJSONConverter.createDefaultFile(options, name);


            } else if(defaults != null) {

                options.onlyOverride = defaults.onlyOverwrite();
                options.HEIGHT = defaults.getHeight();
                options.OFFSET = defaults.getOffset();

                for(int floor : defaults.floors()) {
                    RandomCollectionJson<BlockInfo> info = new RandomCollectionJson<>(BlockInfo.class);
                    defaults.registerBlocks(info, floor);
                    options.BLOCKS.put(floor, info);
                    options.FILL_BLOCK.put(floor, defaults.getFillState(floor));
                }

                defaults.registerLoot(options.LOOT);
                defaults.registerMobs(options.MOBS);

                SkygridJSONConverter.createDefaultFile(options, name);

            }

            OPTIONS.put(name, options);
        }

    }

    public static ResourceLocation[] dimensions() {
        return OPTIONS.keySet().toArray(new ResourceLocation[0]);
    }

}
