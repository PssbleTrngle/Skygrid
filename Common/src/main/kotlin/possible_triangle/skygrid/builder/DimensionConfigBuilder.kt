package possible_triangle.skygrid.builder

import net.minecraft.core.RegistryAccess
import possible_triangle.skygrid.builder.providers.BlockBuilder
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Distance
import possible_triangle.skygrid.data.xml.ListWrapper
import possible_triangle.skygrid.data.xml.impl.SingleBlock

class DimensionConfigBuilder(private val registries: RegistryAccess) {

    companion object {
        fun create(registries: RegistryAccess, builder: DimensionConfigBuilder.() -> Unit): DimensionConfig {
            return DimensionConfigBuilder(registries).apply(builder).build().also {
                it.validate(registries)
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