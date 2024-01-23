package com.possible_triangle.skygrid

import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import com.possible_triangle.skygrid.api.SkygridConstants.MOD_NAME
import com.possible_triangle.skygrid.api.events.RegisterElementEvent
import com.possible_triangle.skygrid.api.events.RegisterModifiersEvent
import com.possible_triangle.skygrid.command.SkygridCommand.readableProbabilities
import com.possible_triangle.skygrid.platform.Services
import com.possible_triangle.skygrid.world.BlockNbtModifiers.registerDefaultModifiers
import com.possible_triangle.skygrid.world.SkygridChunkGenerator
import com.possible_triangle.skygrid.xml.registerDefaultElements
import com.possible_triangle.skygrid.xml.resources.GridConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.ChatFormatting
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Blocks
import org.apache.logging.log4j.LogManager

object SkygridMod {

    val LOGGER = LogManager.getLogger(MOD_NAME)!!

    val STIFF_AIR = Blocks.VOID_AIR
    //val STIFF_AIR by Services.PLATFORM.createBlock("stiff_air") { StiffAir() }

    val GENERATOR_KEY = ResourceKey.create(Registry.CHUNK_GENERATOR_REGISTRY, ResourceLocation(MOD_ID, MOD_ID))

    fun init() {
        registerEvents()
        Presets.register()
        GridConfigs.register()
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
        Registry.register(Registry.CHUNK_GENERATOR, GENERATOR_KEY, SkygridChunkGenerator.CODEC)
    }

    fun onItemTooltip(stack: ItemStack, flags: TooltipFlag, tooltip: MutableList<Component>) {
        val item = stack.item
        if (flags.isAdvanced && item is BlockItem) {

            if (Services.CONFIGS.client.showProbabilities) {
                val probabilities = GridConfigs.getProbability(item.block)
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