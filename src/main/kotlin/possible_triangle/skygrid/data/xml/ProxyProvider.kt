package possible_triangle.skygrid.data.xml

import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.world.BlockAccess
import kotlin.random.Random

abstract class ProxyProvider : BlockProvider() {

    abstract fun get(random: Random): BlockProvider

    final override fun base(random: Random): Block {
        return get(random).base(random)
    }

    override fun generateBase(random: Random, chunk: BlockAccess) {
        get(random).generate(random, chunk)
    }

}