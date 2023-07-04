package com.possible_triangle.skygrid.forge.platform

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import com.possible_triangle.skygrid.platform.services.IConfig


class ForgeConfig : IConfig {

    override val showBlockTags get() = Companion.showBlockTags.get()!!
    override val showProbabilities get() = Companion.showProbabilities.get()!!

    companion object {

        private lateinit var showBlockTags: ForgeConfigSpec.BooleanValue
        private lateinit var showProbabilities: ForgeConfigSpec.BooleanValue

        fun register() {
            val builder = ForgeConfigSpec.Builder()

            showBlockTags = builder.comment("Show block tags in item tooltips")
                .define("showBlockTags", false)

            showProbabilities = builder.comment("Show generation probabilities in item tooltips")
                .define("showProbabilities", false)

            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build())
        }
    }

}