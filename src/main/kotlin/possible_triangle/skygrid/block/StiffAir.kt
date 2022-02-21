package possible_triangle.skygrid.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class StiffAir : Block(Properties.of(Material.STRUCTURAL_AIR).noOcclusion().noCollission().noDrops()) {

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.INVISIBLE
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        ctx: CollisionContext
    ): VoxelShape {
        return Shapes.empty()
    }

    override fun canBeReplaced(state: BlockState, context: BlockPlaceContext): Boolean {
        return true
    }

}