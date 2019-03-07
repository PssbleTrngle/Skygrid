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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SkygridOptions {

    private final HashMap<Integer, RandomCollection<BlockInfo>> BLOCKS = new HashMap<>();
    private final BlockPos OFFSET;
    private final HashMap<Integer, IBlockState> FILL_BLOCK = new HashMap<>();
    private final int HEIGHT;
    private final RandomCollection<ResourceLocation> MOBS = new RandomCollection<>();
    private final RandomCollection<ResourceLocation> LOOT = new RandomCollection<>();

    private static final HashMap<ResourceLocation, SkygridOptions> OPTIONS = new HashMap<>();

    private SkygridOptions(BlockPos offset, int height) {
        this.HEIGHT = height;
        this.OFFSET = offset;
    }

    private static RandomCollection<BlockInfo> forDimension(ResourceLocation dimensionID, int atY) {
        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        Defaults defaults = GameRegistry.findRegistry(Defaults.class).getValue(dimensionID);
        if(defaults == null) {
            Skygrid.LOGGER.error("Could not find default for \"{}\"", dimensionID);
            return null;
        }

        for(int i = defaults.floors().length-1; i>=0; i--)
            if(defaults.floors()[i] <= atY)
                return OPTIONS.get(dimensionID).BLOCKS.get(defaults.floors()[i]);

        return null;
    }

    public static BlockPos getOffset(ResourceLocation dimensionID) {

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

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return Math.max(OPTIONS.get(dimensionID).HEIGHT, 4);
    }

    public static RandomCollection<ResourceLocation> getLoot(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return OPTIONS.get(dimensionID).LOOT;
    }

    public static RandomCollection<ResourceLocation> getMobs(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether"))
            dimensionID = new ResourceLocation(dimensionID.getResourceDomain(), "the_nether");

        return OPTIONS.get(dimensionID).MOBS;
    }

    public static IBlockState getFillBlock(ResourceLocation dimensionID, int atY) {

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

    public static BlockInfo next(ResourceLocation dimensionType, Random random, int atY) {

        RandomCollection<BlockInfo> blocks = forDimension(dimensionType, atY);
        if(blocks == null) return new BlockInfo().add(Blocks.AIR);

        BlockInfo info = blocks.next(random);
        if(info == null)
            throw new NullPointerException("No blocks defined for \"" + dimensionType.toString() + "\"");

        return info;
    }

    public static void reload() {

        IForgeRegistry<Defaults> REGISTRY = GameRegistry.findRegistry(Defaults.class);

        for(Defaults defaults : REGISTRY) {

            ResourceLocation name = REGISTRY.getKey(defaults);
            if(name == null || Arrays.asList(ConfigSkygrid.BLACKLISTED).contains(name.getResourcePath()))
                continue;

            SkygridOptions options = OPTIONS.containsKey(name) ? OPTIONS.get(name) : new SkygridOptions(defaults.getOffset(), defaults.getHeight());

            options.BLOCKS.clear();
            for(int floor : defaults.floors()) {
                RandomCollection<BlockInfo> info = new RandomCollection<>();
                defaults.registerBlocks(info, floor);
                options.BLOCKS.put(floor, info);
                options.FILL_BLOCK.put(floor, defaults.getFillState(floor));
            }

            options.LOOT.clear();
            defaults.registerLoot(options.LOOT);
            options.MOBS.clear();
            defaults.registerMobs(options.MOBS);

            OPTIONS.put(name, options);

        }

    }

    public static ResourceLocation[] dimensions() {
        return OPTIONS.keySet().toArray(new ResourceLocation[0]);
    }

}
