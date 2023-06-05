package possible_triangle.skygrid.data.generation.builder

import possible_triangle.skygrid.data.generation.builder.providers.BlockBuilder
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Distance
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.SingleBlock

class DimensionConfigBuilder {

    var distance: Distance = Distance.DEFAULT
    private val blocks = BasicBlocksBuilder()
    private val loot = LootBuilder()
    private val mobs = MobsBuilder()
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
        gap = BlockBuilder(id, mod).build()
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