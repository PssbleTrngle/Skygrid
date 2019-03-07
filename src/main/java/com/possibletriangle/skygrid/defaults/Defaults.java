package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.blocks.BlockFrame;
import com.possibletriangle.skygrid.generation.WorldProviderSkygrid;
import com.possibletriangle.skygrid.generation.WorldProviderSkygridOverwritten;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.*;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public abstract class Defaults implements IForgeRegistryEntry<Defaults> {

    public Class<? extends WorldProvider> providerClass(boolean overwriting) {
        return overwriting ? WorldProviderSkygridOverwritten.class : WorldProviderSkygrid.class;
    }

    public static final Block FRAME_BLOCK = BlockFrame.FRAME;

    public int getHeight() {
        return Math.min(150, Skygrid.WORLD_HEIGHT);
    }

    public boolean onlyOverwrite() {
        return true;
    }

    public IBlockState getFillState(int floor) {
        return Blocks.AIR.getDefaultState();
    }

    public int getOffset(EnumFacing.Axis axis) {
        return 4;
    }

    public final BlockPos getOffset() {
        return new BlockPos(getOffset(EnumFacing.Axis.X), getOffset(EnumFacing.Axis.Y), getOffset(EnumFacing.Axis.Z));
    }

    private ResourceLocation name;

    @SubscribeEvent
    public static void registerAll(RegistryEvent.Register<Defaults> event) {

        Skygrid.LOGGER.info("Registering Defaults");

        event.getRegistry().register(new DefaultsLimbo().setRegistryName(new ResourceLocation( Skygrid.MODID, "limbo")));
        event.getRegistry().register(new DefaultsCave().setRegistryName(new ResourceLocation( Skygrid.MODID, "cave")));
        event.getRegistry().register(new DefaultsOcean().setRegistryName(new ResourceLocation( Skygrid.MODID, "ocean")));

        event.getRegistry().register(new DefaultsOverworld().setRegistryName(new ResourceLocation("overworld")));
        event.getRegistry().register(new DefaultsNether().setRegistryName(new ResourceLocation("the_nether")));
        event.getRegistry().register(new DefaultsEnd().setRegistryName(new ResourceLocation("the_end")));
        event.getRegistry().register(new DefaultsAetherI().setRegistryName(new ResourceLocation("AetherI")));
        event.getRegistry().register(new DefaultsErebus().setRegistryName(new ResourceLocation("EREBUS")));
        event.getRegistry().register(new DefaultsTeletory().setRegistryName(new ResourceLocation("teletory")));
        event.getRegistry().register(new DefaultsTwilight().setRegistryName(new ResourceLocation("twilight_forest")));
        event.getRegistry().register(new DefaultsTropics().setRegistryName(new ResourceLocation("Tropics")));

        //event.getRegistry().register(new DefaultsOres(true).setRegistryName(new ResourceLocation( Skygrid.MODID, "ores")));

    }

    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        net.minecraftforge.registries.RegistryBuilder<Defaults> builder = new net.minecraftforge.registries.RegistryBuilder<>();
        builder.setType(Defaults.class);
        ResourceLocation key = new ResourceLocation(Skygrid.MODID, "defaults");
        builder.setName(key);
        builder.setDefaultKey(key);
        builder.create();
    }

    public abstract void registerBlocks(RandomCollection<BlockInfo> blocks, int floor);

    public abstract void registerLoot(RandomCollection<ResourceLocation> tables);

    public abstract void registerMobs(RandomCollection<ResourceLocation> mob);

    @Override
    public Defaults setRegistryName(ResourceLocation name) {
        this.name = new ResourceLocation("minecraft", name.getResourcePath());
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<Defaults> getRegistryType() {
        return Defaults.class;
    }

    public int[] floors() {
        return new int[] {0};
    }

}
