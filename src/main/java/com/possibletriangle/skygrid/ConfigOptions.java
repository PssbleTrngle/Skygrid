package com.possibletriangle.skygrid;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigOptions extends Configuration {

    public static String[] BLACKLISTED = new String[0];
    public static int LOWER = -30, UPPER = 10;
    public static float VOID_CHANCE = 0.1F, LIMBO_CHANCE_FALL = 0.1F, LIMBO_CHANCE_CLIMB = 0.1F;

    private static ConfigOptions config;

    public ConfigOptions(File file) {
        super(file);
    }

    public static void init(File dir) {
        File file = new File(dir.getPath() + File.separator + Skygrid.MODID + File.separator + "config.cfg");
        Skygrid.LOGGER.info("Config file path: {}", file.getAbsolutePath());
        config = new ConfigOptions(file);
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

        String FALL = "Falling";
        VOID_CHANCE = getFloat("void_chance", FALL, VOID_CHANCE, 0, 1, "The chance that a players just falls to death instead of traveling somewhere when falling [0 to disable]");
        LIMBO_CHANCE_FALL = getFloat("limbo_chance_fall", FALL, LIMBO_CHANCE_FALL, 0, 1, "The chance that a players travels to the limbo when falling [0 to disable]");
        LIMBO_CHANCE_CLIMB = getFloat("limbo_chance_climb", FALL, LIMBO_CHANCE_CLIMB, 0, 1, "The chance that a players travels to the limbo when climbing [0 to disable]");
        UPPER = getInt("upper", FALL, UPPER, 0, 242, "The amount of blocks on has to be above building height to climb into the dimension above");
        LOWER = getInt("lower", FALL, LOWER, -242, 0, "The amount of blocks on has to be below zero to fall into the dimension below");
    }
}
