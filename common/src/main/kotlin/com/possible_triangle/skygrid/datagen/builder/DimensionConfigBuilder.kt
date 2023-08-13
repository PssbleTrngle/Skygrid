package com.possible_triangle.skygrid.datagen.builder

import com.possible_triangle.skygrid.api.xml.IReferenceContext
import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.api.xml.elements.ListWrapper
import com.possible_triangle.skygrid.api.xml.elements.providers.SingleBlock
import com.possible_triangle.skygrid.datagen.DatagenContext
import com.possible_triangle.skygrid.datagen.builder.providers.BlockBuilder
import com.possible_triangle.skygrid.xml.ReferenceContext

class DimensionConfigBuilder(private val context: DatagenContext) {

    companion object {
        fun create(
            context: DatagenContext,
            references: IReferenceContext = ReferenceContext(),
            builder: DimensionConfigBuilder.() -> Unit,
        ): DimensionConfig {
            return DimensionConfigBuilder(context).apply(builder).build().also {
                it.validate(context.registries, references)
            }
        }
    }

    var distance: Distance = Distance.DEFAULT
    private val blocks = BasicBlocksBuilder(context)
    private val loot = LootBuilder(context)
    private val mobs = MobsBuilder(context)
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

    fun gap(id: String, mod: String = context.defaultMod) {
        gap = BlockBuilder(context, id, mod).build()
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