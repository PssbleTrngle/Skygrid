package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.impl.SingleBlock
import com.possible_triangle.skygrid.datagen.builder.providers.BlockBuilder
import com.possible_triangle.skygrid.xml.ReferenceContext
import net.minecraft.core.RegistryAccess

class DimensionConfigBuilder(private val registries: RegistryAccess) {

    companion object {
        fun create(
            registries: RegistryAccess,
            references: IReferenceContext = ReferenceContext(),
            builder: DimensionConfigBuilder.() -> Unit,
        ): DimensionConfig {
            return DimensionConfigBuilder(registries).apply(builder).build().also {
                it.validate(registries, references)
            }
        }
    }

    var distance: Distance = Distance.DEFAULT
    private val blocks = BasicBlocksBuilder(registries)
    private val loot = LootBuilder()
    private val mobs = MobsBuilder(registries)
    private var gap: SingleBlock? = null

    fun blocks(builder: IBlocksBuilder.() -> Unit) {
        builder(blocks)
    }

    fun loot(builder: LootBuilder.() -> Unit) {
        builder(loot)
    }

    fun mobs(builder: MobsBuilder.() -> Unit) {
        builder(mobs)
    }

    fun gap(id: String, mod: String = "minecraft") {
        gap = BlockBuilder(id, mod, registries = registries).build()
    }

    fun build(): DimensionConfig {
        return DimensionConfig(
            blocks = ListWrapper(blocks.build()),
            loot = loot.build(),
            mobs = mobs.build(),
            distance = distance,
            unsafeGap = gap,
        )
    }

}