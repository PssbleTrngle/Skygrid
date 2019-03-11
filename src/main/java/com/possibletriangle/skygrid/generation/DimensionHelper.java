package com.possibletriangle.skygrid.generation;

import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.defaults.Defaults;
import com.possibletriangle.skygrid.random.SkygridOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;

public class DimensionHelper {

     static final HashMap<ResourceLocation, WorldProvider> OLD = new HashMap<>();

    public static void overwriteDimensions() {

        for(ResourceLocation dim : SkygridOptions.dimensions())
            register(dim);

    }

    private static void register(ResourceLocation dim) {

        Defaults defaults = GameRegistry.findRegistry(Defaults.class).getValue(dim);

        int ID = getIDFor(dim);
        if(ID != DimensionManager.getNextFreeDimId()) {

            Skygrid.LOGGER.info("Overwriting dimension {}", dim.getResourcePath());
            WorldProvider old = DimensionManager.getProviderType(ID).createDimension();
            OLD.put(dim, old);
            DimensionManager.unregisterDimension(ID);
            Class clazz = defaults == null ? WorldProviderSkygrid.class : defaults.providerClass(true);
            DimensionManager.registerDimension(ID, DimensionType.register(dim.getResourcePath(), "_" + dim.getResourcePath(), ID, clazz, false));

        } else if(!SkygridOptions.onlyOverride(dim)) {

            Skygrid.LOGGER.info("Creating dimension {}", dim.getResourcePath());
            Class clazz = defaults == null ? WorldProviderSkygrid.class : defaults.providerClass(false);
            DimensionManager.registerDimension(ID, DimensionType.register(dim.getResourcePath(), "_" + dim.getResourcePath(), ID, clazz, false));

        } else
            Skygrid.LOGGER.info("There is no dimension with the name {}", dim.getResourcePath());

    }

    public static int getIDFor(ResourceLocation dimensionID) {

        if(dimensionID.getResourcePath().toLowerCase().equals("nether") || dimensionID.getResourcePath().toLowerCase().equals("the_nether"))
            return -1;

        for(DimensionType type : DimensionManager.getRegisteredDimensions().keySet())
            if(type.getName().toLowerCase().equals(dimensionID.getResourcePath().toLowerCase())) return type.getId();

        return DimensionManager.getNextFreeDimId();

    }

}
