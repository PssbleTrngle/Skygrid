package possible_triangle.skygrid.data.generation.builder

import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Distance
import possible_triangle.skygrid.data.xml.ListWrapper

class DimensionConfigBuilder {

    var distance: Distance = Distance.DEFAULT
    private val blocks = BasicBlocksBuilder()
    private val loot = LootBuilder()

    fun blocks(builder: IBlocksBuilder.() ->  Unit) {
        builder(blocks)
    }

    fun loot(builder: LootBuilder.() -> Unit) {
        builder(loot)
    }

    fun build(): DimensionConfig {
        return DimensionConfig(
            blocks = ListWrapper(blocks.build()),
            loot = loot.build(),
            distance = distance,
        )
    }

}