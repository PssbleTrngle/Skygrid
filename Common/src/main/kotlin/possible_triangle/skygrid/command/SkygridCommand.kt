package possible_triangle.skygrid.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import possible_triangle.skygrid.Constants
import possible_triangle.skygrid.data.xml.DimensionConfig
import possible_triangle.skygrid.data.xml.Distance
import possible_triangle.skygrid.world.BlockAccess
import kotlin.random.Random

inline fun <T> tryOr(supplier: () -> T, default: () -> T): T {
    return try {
        supplier()
    } catch (ex: IllegalArgumentException) {
        default()
    }
}

object SkygridCommand {

    private val UNKNOWN_CONFIG =
        DynamicCommandExceptionType { TranslatableComponent("commands.skygrid.unknown_config") }

    private val AIR = Blocks.AIR.defaultBlockState()

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

        val configArgument = {
            argument("config", StringArgumentType.string()).suggests { _, builder ->
                SharedSuggestionProvider.suggest(
                    DimensionConfig.keys.map { it.toString().replace("minecraft:", "") }, builder
                )
            }
        }

        val singleBlock = argument("start", BlockPosArgument.blockPos()).executes(::generate).then(
            configArgument().executes(::generate)
                .then(argument("seed", LongArgumentType.longArg()).executes(::generate))
        )

        val range = argument("end", BlockPosArgument.blockPos()).executes(::generateRange).then(
            configArgument().executes(::generateRange).then(
                argument("snap", BoolArgumentType.bool()).executes(::generateRange).then(
                    literal("random").executes(::generateRange)
                        .then(argument("distance", IntegerArgumentType.integer(1)).executes(::generateRange))
                )
                    .then(
                        argument("seed", LongArgumentType.longArg()).executes(::generateRange)
                            .then(argument("distance", IntegerArgumentType.integer(1)).executes(::generateRange))
                    )
            )
        )

        dispatcher.register(literal(Constants.MOD_ID).then(literal("generate").then(singleBlock.then(range))))
    }

    private fun getConfig(ctx: CommandContext<CommandSourceStack>): DimensionConfig {
        return tryOr({
            val key = StringArgumentType.getString(ctx, "config")
            DimensionConfig[ResourceLocation(key)] ?: throw UNKNOWN_CONFIG.create(key)
        }) {
            DimensionConfig[ResourceLocation("overworld")] ?: DimensionConfig.DEFAULT
        }
    }

    private fun generateAt(config: DimensionConfig, pos: BlockPos, level: ServerLevel, random: Random) {
        config.generate(random, BlockAccess({ state, offset ->
            val at = pos.offset(offset)
            level.setBlock(at, state, 2)
        }, { level.getBlockState(pos.offset(it)) }))
    }

    private fun generateRange(ctx: CommandContext<CommandSourceStack>): Int {
        val config = getConfig(ctx)
        val start = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        val end = BlockPosArgument.getLoadedBlockPos(ctx, "end")
        val level = ctx.source.level

        val snap = tryOr({ BoolArgumentType.getBool(ctx, "snap") }) { false }
        val random = tryOr({ LongArgumentType.getLong(ctx, "seed").let(::Random) }, { Random })
        val distance =
            tryOr({ IntegerArgumentType.getInteger(ctx, "distance").let(Distance::of) }) { config.distance }

        val origin = if (snap) start else BlockPos(0, 0, 0)

        val replaced = BlockPos.betweenClosed(start.offset(-1, -1, -1), end.offset(1, 1, 1))
            .map { BlockPos(it) }
            .filterNot { level.getBlockState(it).isAir }
            .onEach { level.setBlock(it, AIR, 2) }

        val generated = BlockPos.betweenClosed(start, end)
            .map { BlockPos(it) }
            .filter {
                it.subtract(origin).x % distance.x == 0
                        && it.subtract(origin).y % distance.y == 0
                        && it.subtract(origin).z % distance.z == 0
            }.onEach { generateAt(config, it, level, random) }

        val changed = replaced + generated
        return changed.onEach { level.blockUpdated(it, level.getBlockState(it).block) }.count()
    }

    private fun generate(ctx: CommandContext<CommandSourceStack>): Int {
        val config = getConfig(ctx)

        val pos = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        val random = tryOr({ LongArgumentType.getLong(ctx, "seed").let(::Random) }, { Random })
        generateAt(config, pos, ctx.source.level, random)

        ctx.source.level.blockUpdated(pos, ctx.source.level.getBlockState(pos).block)

        return 1
    }

}