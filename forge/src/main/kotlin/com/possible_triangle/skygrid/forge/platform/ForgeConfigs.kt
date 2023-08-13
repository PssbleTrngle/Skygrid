package com.possible_triangle.skygrid.forge.platform

import com.possible_triangle.skygrid.platform.services.IConfigs
import com.possible_triangle.skygrid.world.BlockNbtModifiers
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig


class ForgeConfigs : IConfigs {

    override val client = object : IConfigs.IClientConfig {
        override val showBlockTags get() = Companion.showBlockTags.get()!!
        override val showProbabilities get() = Companion.showProbabilities.get()!!
    }

    override val server = object : IConfigs.IServerConfig {
        override val warnInvalid get() = Companion.warnInvalid.get()!!
        override val modifierStrategy get() = Companion.modifierStrategy.get()!!
    }

    companion object {

        private lateinit var showBlockTags: ForgeConfigSpec.BooleanValue
        private lateinit var showProbabilities: ForgeConfigSpec.BooleanValue

        private lateinit var warnInvalid: ForgeConfigSpec.BooleanValue
        private lateinit var modifierStrategy: ForgeConfigSpec.EnumValue<BlockNbtModifiers.ModifierStrategy>

        fun register() {
            ForgeConfigSpec.Builder().apply {
                showBlockTags = comment("Show block tags in item tooltips")
                    .define("showBlockTags", false)

                showProbabilities = comment("Show generation probabilities in item tooltips")
                    .define("showProbabilities", false)

                ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, build())
            }

            ForgeConfigSpec.Builder().apply {
                warnInvalid = comment("Print warnings about invalid entries into the log")
                    .define("warnInvalid", false)

                modifierStrategy = comment("strategy in which block nbt modifiers are applied")
                    .defineEnum("modifierStrategy", BlockNbtModifiers.ModifierStrategy.FIRST_ONLY)

                ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, build())
            }
        }
    }

}