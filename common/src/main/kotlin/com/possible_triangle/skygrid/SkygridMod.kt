package com.possible_triangle.skygrid

import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import com.possible_triangle.skygrid.api.SkygridConstants.MOD_NAME
import com.possible_triangle.skygrid.api.events.RegisterElementEvent
import com.possible_triangle.skygrid.api.events.RegisterModifiersEvent
import com.possible_triangle.skygrid.block.StiffAir
import com.possible_triangle.skygrid.command.SkygridCommand.readableProbabilities
import com.possible_triangle.skygrid.platform.Services
import com.possible_triangle.skygrid.world.BlockNbtModifiers.registerDefaultModifiers
import com.possible_triangle.skygrid.world.SkygridChunkGenerator
import com.possible_triangle.skygrid.xml.registerDefaultElements
import com.possible_triangle.skygrid.xml.resources.DimensionConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.ChatFormatting
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import org.apache.logging.log4j.LogManager

object SkygridMod {

    val LOGGER = LogManager.getLogger(MOD_NAME)!!

    val STIFF_AIR by Services.PLATFORM.createBlock("stiff_air") { StiffAir() }

    fun init() {
        registerEvents()
        Presets.register()
        DimensionConfigs.register()
    }

    private fun registerEvents() {
        RegisterElementEvent.EVENT.addListener {
            it.registerDefaultElements()
        }

        RegisterModifiersEvent.EVENT.addListener {
            it.registerDefaultModifiers()
        }
    }

    fun setup() {
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(MOD_ID, MOD_ID), SkygridChunkGenerator.CODEC)
    }

    fun onItemTooltip(stack: ItemStack, flags: TooltipFlag, tooltip: MutableList<Component>) {
        val item = stack.item
        if (flags.isAdvanced && item is BlockItem) {

            if (Services.CONFIGS.client.showProbabilities) {
                val probabilities = DimensionConfigs.getProbability(item.block)
                if (probabilities.isNotEmpty()) {
                    tooltip.add(Component.literal("Probabilities:").withStyle(ChatFormatting.GOLD))
                    tooltip.addAll(probabilities.readableProbabilities())
                }
            }

            if (Services.CONFIGS.client.showBlockTags) {
                Services.PLATFORM.getTags(item.block).forEach {
                    tooltip.add(Component.literal("#$it"))
                }
            }

        }
    }

}