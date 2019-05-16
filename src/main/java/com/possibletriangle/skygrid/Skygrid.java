package com.possibletriangle.skygrid;

import com.possibletriangle.skygrid.blocks.BlockFrame;
import com.possibletriangle.skygrid.generation.DimensionHelper;
import com.possibletriangle.skygrid.random.SkygridOptions;
import com.possibletriangle.skygrid.travel.TravelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = Skygrid.MODID, name = Skygrid.NAME, version = Skygrid.VERSION, dependencies = Skygrid.DEPENDENCIES)
public class Skygrid {

    public static int WORLD_HEIGHT = 255;

    public static final String MODID = "skygrid";
    public static final String NAME = "Skygrid";
    public static final String VERSION = "1.4.2";
    public static final String DEPENDENCIES
            = "after:biomesoplenty;"
            + "after:natura;"
            + "after:aether_legacy;"
            + "after:teletoro;"
            + "after:twilightforest;";

    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        ConfigSkygrid.init(event.getModConfigurationDirectory());
        ConfigSkygrid.reload();
        BlockFrame.FRAME = new BlockFrame();
    }

    @EventHandler
    public void setup(FMLInitializationEvent event){
        LOGGER.info("Skygrid says hi!");

        SkygridOptions.reload(false);
        TravelManager.registerDefaults();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event){
        DimensionHelper.overwriteDimensions();
        TravelManager.validate();
        SkygridOptions.validate();
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent  event) {
        event.registerServerCommand(new CommandReload());
    }

    @SideOnly(Side.CLIENT)
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

        if(event.getEntityLiving() instanceof EntityPlayer)
            if(event.getDistance() <= ConfigSkygrid.FALL_CAP)
                event.setDamageMultiplier(0);
            else
                event.setDamageMultiplier(event.getDamageMultiplier() * ConfigSkygrid.FALL_FACTOR);

    }

}
