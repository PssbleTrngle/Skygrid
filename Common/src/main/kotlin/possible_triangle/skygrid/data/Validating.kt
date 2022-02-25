package possible_triangle.skygrid.data

import net.minecraft.core.Registry
import net.minecraft.tags.TagContainer
import net.minecraft.world.level.block.Block

interface Validating {

    fun validate(blocks: Registry<Block>, tags: TagContainer, references: ReferenceContext): Boolean

}