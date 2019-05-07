package com.possibletriangle.skygrid.defaults;

import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.defaults.modded.*;
import com.possibletriangle.skygrid.defaults.modded.adventofascension.*;
import com.possibletriangle.skygrid.defaults.vanilla.*;
import com.possibletriangle.skygrid.generation.WorldProviderSkygrid;
import com.possibletriangle.skygrid.generation.WorldProviderSkygridOverwritten;
import com.possibletriangle.skygrid.random.BlockInfo;
import com.possibletriangle.skygrid.random.RandomCollection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public abstract class Defaults implements IForgeRegistryEntry<Defaults> {

    public Class<? extends WorldProvider> providerClass(boolean overwriting) {
        return overwriting ? WorldProviderSkygridOverwritten.class : WorldProviderSkygrid.class;
    }

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

        if(Loader.isModLoaded("extrautils2"))
            event.getRegistry().register(new DefaultsDeepDark().setRegistryName(new ResourceLocation( "Deep Dark")));
        else if(Loader.isModLoaded("beneath"))
            event.getRegistry().register(new DefaultsCave().setRegistryName(new ResourceLocation("The Beneath")));
        else
            event.getRegistry().register(new DefaultsCave().setRegistryName(new ResourceLocation( Skygrid.MODID, "cave")));

        event.getRegistry().register(new DefaultsOcean().setRegistryName(new ResourceLocation( Skygrid.MODID, "ocean")));

        event.getRegistry().register(new DefaultsOverworld().setRegistryName(new ResourceLocation("overworld")));
        event.getRegistry().register(new DefaultsNether().setRegistryName(new ResourceLocation("the_nether")));
        event.getRegistry().register(new DefaultsEnd().setRegistryName(new ResourceLocation("the_end")));

        if(Loader.isModLoaded("huntingdim")) event.getRegistry().register(new DefaultsHunting().setRegistryName(new ResourceLocation("hunting_dim")));
        if(Loader.isModLoaded("aether_legacy")) event.getRegistry().register(new DefaultsAetherI().setRegistryName(new ResourceLocation("AetherI")));
        if(Loader.isModLoaded("erebus")) event.getRegistry().register(new DefaultsErebus().setRegistryName(new ResourceLocation("EREBUS")));
        if(Loader.isModLoaded("teletoro")) event.getRegistry().register(new DefaultsTeletory().setRegistryName(new ResourceLocation("teletory")));
        if(Loader.isModLoaded("twilightforest")) event.getRegistry().register(new DefaultsTwilight().setRegistryName(new ResourceLocation("twilight_forest")));
        if(Loader.isModLoaded("tropicraft")) event.getRegistry().register(new DefaultsTropics().setRegistryName(new ResourceLocation("Tropics")));

        if(Loader.isModLoaded("aoa3")) {
            event.getRegistry().register(new DefaultsImmortalis().setRegistryName(new ResourceLocation("immortallis")));
            event.getRegistry().register(new DefaultsIromine().setRegistryName(new ResourceLocation("iromine")));
            event.getRegistry().register(new DefaultsHaven().setRegistryName(new ResourceLocation("haven")));
            event.getRegistry().register(new DefaultsAbyss().setRegistryName(new ResourceLocation("abyss")));
            event.getRegistry().register(new DefaultsPrecasia().setRegistryName(new ResourceLocation("precasia")));
            event.getRegistry().register(new DefaultsCandyland().setRegistryName(new ResourceLocation("candyland")));
        }

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
