package possible_triangle.skygrid.command

import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import possible_triangle.skygrid.SkygridMod
import possible_triangle.skygrid.config.DimensionConfig
import possible_triangle.skygrid.world.BlockAccess
import kotlin.random.Random

@Mod.EventBusSubscriber
object SkygridCommand {

    private val AIR = Blocks.AIR.defaultBlockState()

    @SubscribeEvent
    fun register(event: RegisterCommandsEvent) {
        event.dispatcher.register(literal(SkygridMod.ID).then(literal("generate")
            .then(argument("start", BlockPosArgument.blockPos())
                .then(argument("end", BlockPosArgument.blockPos())
                    .executes(::generateRange))
                .executes(::generate))))
    }

    private fun getConfig(ctx: CommandContext<CommandSourceStack>): DimensionConfig {
        return DimensionConfig[ResourceLocation("overworld")] ?: throw NullPointerException()
    }

    private fun generateAt(config: DimensionConfig, pos: BlockPos, level: ServerLevel) {
        config.generate(Random, BlockAccess({ state, offset ->
            val at = pos.offset(offset)
            level.setBlock(at, state, 2)
        }, { level.getBlockState(pos).isAir }))
    }

    private fun generateRange(ctx: CommandContext<CommandSourceStack>): Int {
        val config = getConfig(ctx)
        val start = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        val end = BlockPosArgument.getLoadedBlockPos(ctx, "end")
        val level = ctx.source.level

        val range = BlockPos.betweenClosed(start, end).map { BlockPos(it) }

        val changed = range
            .filterNot { level.getBlockState(it).isAir }
            .onEach { level.setBlock(it, AIR, 2) } + range
            .filter { it.x % config.distance.x == 0 && it.y % config.distance.y == 0 && it.z % config.distance.z == 0 }
            .onEach { generateAt(config, it, level) }

        return changed.onEach { level.blockUpdated(it, level.getBlockState(it).block) }.count()
    }

    private fun generate(ctx: CommandContext<CommandSourceStack>): Int {
        val config = getConfig(ctx)

        val pos = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        generateAt(config, pos, ctx.source.level)

        ctx.source.level.blockUpdated(pos, ctx.source.level.getBlockState(pos).block)

        return 1
    }

}