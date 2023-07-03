package possible_triangle.skygrid.extensions

import net.minecraft.world.level.block.Block
import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.flat

fun Collection<BlockProvider>.weights(): Map<Block, Double> {
    val flat = this.flat()
    return hashMapOf<Block, Double>().apply {
        flat.forEach { (block, weight) ->
            put(block, getOrDefault(block, 0.0) + weight)
        }
    }
}