package com.possible_triangle.skygrid.xml.resources

import com.possible_triangle.skygrid.api.extensions.flat
import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.impl.SingleBlock
import com.possible_triangle.skygrid.xml.ReferenceContext
import com.possible_triangle.skygrid.xml.XMLResource
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.Block

object DimensionConfigs : XMLResource<DimensionConfig>("dimensions", { DimensionConfig.serializer() }) {

    val DEFAULT = DimensionConfig(ListWrapper(SingleBlock("bedrock")))

    private val WEIGHT_MAP = hashMapOf<Block, HashMap<ResourceLocation, Double>>()

    fun getProbability(block: Block): Map<ResourceLocation, Double> {
        return WEIGHT_MAP[block] ?: emptyMap()
    }

    override fun merge(a: DimensionConfig, b: DimensionConfig): DimensionConfig {
        return if (b.replace) b
        else b.copy(
            replace = false,
            blocks = a.blocks + b.blocks,
            loot = a.loot + b.loot,
            mobs = a.mobs + b.mobs,
        )
    }

    override fun onReload(server: MinecraftServer) {
        validate(DEFAULT, server)

        entries.forEach { (key, config) ->
            val blocks = config.blocks.flat()
            blocks.forEach { (block, probability) ->
                WEIGHT_MAP.getOrPut(block) { hashMapOf() }[key] = probability
            }
        }
    }

    override fun validate(value: DimensionConfig, server: MinecraftServer): Boolean {
        val registries = server.registryAccess()
        val references = ReferenceContext()
        return value.validate(registries, references)
    }

}