package com.possibletriangle.skygrid;

import com.possibletriangle.skygrid.generation.DimensionHelper;
import com.possibletriangle.skygrid.random.RandomManager;
import com.possibletriangle.skygrid.travel.TravelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;

@Mod.EventBusSubscriber
@Mod(modid = Skygrid.MODID, name = Skygrid.NAME, version = Skygrid.VERSION, dependencies = Skygrid.DEPENDENCIES)
public class Skygrid {

    public static int WORLD_HEIGHT = 255;

    public static final String MODID = "skygrid";
    public static final String NAME = "Skygrid";
    public static final String VERSION = "1.0";
    public static final String DEPENDENCIES = "after:biomesoplenty;after:natura;after:aether_legacy;after:teletoro;after:twilightforest;";

    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        ConfigOptions.init(event.getModConfigurationDirectory());
        ConfigOptions.reload();
    }

    @EventHandler
    public void setup(FMLInitializationEvent event){
        LOGGER.info("Skygrid says hi!");

        RandomManager.reload();
        TravelManager.registerDefaults();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event){
        DimensionHelper.overwriteDimensions();
        TravelManager.validate();
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent  event) {
        event.registerServerCommand(new CommandReload());
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {

        if(event.getItemStack().isEmpty() || !event.getFlags().isAdvanced())
            return;

        for(int id : OreDictionary.getOreIDs(event.getItemStack())) {
            event.getToolTip().add(TextFormatting.ITALIC + OreDictionary.getOreName(id) + TextFormatting.RESET);
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fall(LivingFallEvent event) {

        if(event.getEntityLiving() instanceof EntityPlayer) {

            if(event.getDistance() <= 4.5)
                event.setDamageMultiplier(0);

        }
    }

}
