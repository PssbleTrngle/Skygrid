package com.possibletriangle.skygrid;

import com.possibletriangle.skygrid.travel.TravelManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;

public class ConfigSkygrid extends Configuration {

    public static float FALL_FACTOR = 0.8F;
    public static String[] BLACKLISTED = new String[0];
    public static int LOWER = -30, UPPER = 10;
    public static float VOID_CHANCE = 0.1F, LIMBO_CHANCE_FALL = 0.1F, LIMBO_CHANCE_CLIMB = 0.1F;

    private static ConfigSkygrid config;

    public ConfigSkygrid(File file) {
        super(file);
    }

    public static void init(File dir) {
        File file = new File(dir.getPath() + File.separator + Skygrid.MODID + File.separator + "config.cfg");
        Skygrid.LOGGER.info("Config file path: {}", file.getAbsolutePath());
        config = new ConfigSkygrid(file);
    }

    public static void reload() {
        Skygrid.LOGGER.info("Loading config...");
        config.load();
        config.save();
    }

    @Override
    public void load() {
        super.load();

        String MAIN = "Main";
        BLACKLISTED = getStringList("blacklist", MAIN, BLACKLISTED, "Dimension for that skygrid creation/overwriting is disabled");

        FALL_FACTOR = getFloat("fall_damage_factor", MAIN, FALL_FACTOR, 0, 1, "This factor is applied to player fall damage. " + FALL_FACTOR + " protects one from a 4 block fall");

        String FALL = "Falling";
        VOID_CHANCE = getFloat("void_chance", FALL, VOID_CHANCE, 0, 1, "The chance that a players just falls to death instead of traveling somewhere when falling [0 to disable]");
        LIMBO_CHANCE_FALL = getFloat("limbo_chance_fall", FALL, LIMBO_CHANCE_FALL, 0, 1, "The chance that a players travels to the limbo when falling [0 to disable]");
        LIMBO_CHANCE_CLIMB = getFloat("limbo_chance_climb", FALL, LIMBO_CHANCE_CLIMB, 0, 1, "The chance that a players travels to the limbo when climbing [0 to disable]");
        UPPER = getInt("upper", FALL, UPPER, 0, 242, "The amount of blocks on has to be above building height to climb into the dimension above");
        LOWER = getInt("lower", FALL, LOWER, -242, 0, "The amount of blocks on has to be below zero to fall into the dimension below");

        String[] default_falls = new String[] {
            "overworld|the_nether",
            "AetherI|overworld",
            "twilight_forest|EREBUS",
            "Tropics|ocean",
            "teletory|the_end"
        };

        for(String fall : getStringList("falls", FALL, default_falls, "The list of dimensions which are below another one. Syntax: \"above|below\""))
            if(fall.split("\\|").length == 2) {

                String[] split = fall.split("\\|");
                TravelManager.registerFall(new ResourceLocation(split[0]), new ResourceLocation(split[1]), false);

            } else
                Skygrid.LOGGER.error("\"{}\" is not a valid dimension fall String", fall);

    }
}
