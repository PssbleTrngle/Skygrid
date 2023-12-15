package com.possible_triangle.skygrid.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.possible_triangle.skygrid.api.SkygridConstants.MOD_ID
import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.api.xml.elements.GridConfig
import com.possible_triangle.skygrid.world.BlockAccess
import com.possible_triangle.skygrid.xml.XMLResource
import com.possible_triangle.skygrid.xml.resources.GridConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.commands.arguments.blocks.BlockInput
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

inline fun <T> tryOr(supplier: () -> T, default: () -> T): T {
    return try {
        supplier()
    } catch (ex: IllegalArgumentException) {
        default()
    }
}

object SkygridCommand {

    private val UNKNOWN_CONFIG =
        DynamicCommandExceptionType { Component.translatable("commands.skygrid.unknown_config") }

    private val UNKNOWN_PRESET =
        DynamicCommandExceptionType { Component.translatable("commands.skygrid.unknown_preset") }

    private val NOT_A_BLOCK =
        DynamicCommandExceptionType { Component.translatable("commands.skygrid.not_a_block") }

    private val AIR = Blocks.AIR.defaultBlockState()

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>, ctx: CommandBuildContext) {
        val resourceArgument = { name: String, resource: XMLResource<*> ->
            argument(name, ResourceLocationArgument.id()).suggests { _, builder ->
                SharedSuggestionProvider.suggest(resource.keys.map { it.toString().replace("minecraft:", "") }, builder)
            }
        }

        val singleBlock = { gen: (CommandContext<CommandSourceStack>) -> Generator<IBlockAccess> ->
            val executor: Command<CommandSourceStack> = Command { generate(it, gen(it)) }
            argument("start", BlockPosArgument.blockPos()).executes(executor)
                .then(argument("seed", LongArgumentType.longArg()).executes(executor))
        }

        val range = { gen: (CommandContext<CommandSourceStack>) -> GridConfig ->
            val executor: Command<CommandSourceStack> = Command { generateRange(it, gen(it)) }
            val tail = { argument("distance", IntegerArgumentType.integer(1)).executes(executor) }
            val range = argument("end", BlockPosArgument.blockPos()).executes(executor).then(

                argument("snap", BoolArgumentType.bool()).executes(executor)
                    .then(literal("random").executes(executor).then(tail()))
                    .then(argument("seed", LongArgumentType.longArg()).executes(executor).then(tail()))

            )
            singleBlock(gen).then(range)
        }

        dispatcher.register(
            literal(MOD_ID).then(
                literal("generate").requires { it.hasPermission(2) }
                    .then(
                        literal("preset").then(
                            resourceArgument("preset", Presets).then(singleBlock {
                                val key = ResourceLocationArgument.getId(it, "preset")
                                Presets[key] ?: throw UNKNOWN_PRESET.create(key)
                            })
                        )
                    )
                    .then(resourceArgument("config", GridConfigs).then(range {
                        val key = ResourceLocationArgument.getId(it, "config")
                        GridConfigs[key] ?: throw UNKNOWN_CONFIG.create(key)
                    }))
            ).then(
                literal("probability")
                    .executes(::showProbability)
                    .then(
                        argument("block", BlockStateArgument.block(ctx))
                            .executes(::showProbability)
                    )
            )
        )
    }

    private fun access(origin: BlockPos, level: ServerLevel): BlockAccess {
        return object : BlockAccess() {
            override fun setBlock(state: BlockState, pos: BlockPos) {
                val at = pos.offset(origin)
                level.setBlock(at, state, 2)
            }

            override fun canReplace(pos: BlockPos): Boolean {
                return level.getBlockState(pos.offset(origin)).isAir
            }

            override fun setNBT(pos: BlockPos, nbt: CompoundTag) {
                val tile = level.getBlockEntity(pos.offset(origin))
                tile?.load(nbt)
            }
        }
    }

    private fun CommandContext<CommandSourceStack>.random() =
        tryOr({ LongArgumentType.getLong(this, "seed").let(RandomSource::create) }, RandomSource::create)

    private fun generateRange(
        ctx: CommandContext<CommandSourceStack>,
        generator: GridConfig,
    ): Int {
        val start = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        val end = BlockPosArgument.getLoadedBlockPos(ctx, "end")
        val level = ctx.source.level

        val snap = tryOr({ BoolArgumentType.getBool(ctx, "snap") }) { false }
        val random = ctx.random()
        val distance =
            tryOr({ IntegerArgumentType.getInteger(ctx, "distance").let(Distance::of) }) { generator.distance }

        val origin = if (snap) start else BlockPos(0, 0, 0)

        val air = BlockInput(AIR, emptySet(), null)
        val replaced = BlockPos.betweenClosed(start.offset(-1, -1, -1), end.offset(1, 1, 1))
            .asSequence()
            .map { BlockPos(it) }
            .sortedByDescending { it.y }
            .sortedBy { distance.isBlock(it.subtract(origin)) }
            .filterNot { level.getBlockState(it).isAir }.onEach { air.place(level, it, 2) }
            .toList()

        val generated = BlockPos.betweenClosed(start, end)
            .map { BlockPos(it) }
            .filter { distance.isBlock(it.subtract(origin)) }
            .onEach { generator.generate(random, access(it, level)) }

        val changed = replaced + generated
        return changed.onEach { level.blockUpdated(it, level.getBlockState(it).block) }.count()
    }

    private fun generate(ctx: CommandContext<CommandSourceStack>, generator: Generator<IBlockAccess>): Int {
        val pos = BlockPosArgument.getLoadedBlockPos(ctx, "start")
        val random = ctx.random()
        generator.generate(random, access(pos, ctx.source.level))

        ctx.source.level.blockUpdated(pos, ctx.source.level.getBlockState(pos).block)

        return 1
    }

    fun Map<ResourceLocation, Double>.readableProbabilities() = map { (config, probability) ->
        val percentage = Component.literal("${String.format("%.3f", probability * 100)}%")
        Component.literal("  $config: ").append(percentage.withStyle(ChatFormatting.AQUA))
    }

    private fun showProbability(ctx: CommandContext<CommandSourceStack>): Int {
        val block = tryOr({
            BlockStateArgument.getBlock(ctx, "block").state.block
        }, {
            val held = ctx.source.playerOrException.mainHandItem
            val item = held.item
            if (item is BlockItem) item.block
            else throw NOT_A_BLOCK.create(held.displayName)
        })

        val probabilities = GridConfigs.getProbability(block)

        when (probabilities.size) {
            0 -> ctx.source.sendFailure(
                Component.translatable("commands.$MOD_ID.no_probability", block.name).withStyle(ChatFormatting.RED)
            )

            1 -> ctx.source.sendSuccess(
                { Component.translatable("commands.$MOD_ID.found_probability", block.name)
                    .withStyle(ChatFormatting.GREEN) }, false
            )

            else -> ctx.source.sendSuccess(
                { Component.translatable(
                    "commands.$MOD_ID.found_probabilities",
                    block.name,
                    probabilities.size
                ).withStyle(ChatFormatting.GREEN) }, false
            )
        }

        probabilities.readableProbabilities().forEach { line ->
            ctx.source.sendSuccess({ line }, false)
        }

        return probabilities.size
    }

}