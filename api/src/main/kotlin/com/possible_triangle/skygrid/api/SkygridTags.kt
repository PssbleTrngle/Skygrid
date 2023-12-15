package com.possible_triangle.skygrid.api

import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object SkygridTags {

    val AMETHYST_CLUSTERS = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "amethyst_clusters"))
    val LOOT_CONTAINERS = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "loot_containers"))
    val BARRELS = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "barrels"))
    val CHESTS = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "chests"))
    val GLASS_SILICA = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "silica_glass"))
    val FOREIGN_ORES = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation(MOD_ID, "foreign_ores"))

}