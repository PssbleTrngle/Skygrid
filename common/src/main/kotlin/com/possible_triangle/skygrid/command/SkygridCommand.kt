package com.possible_triangle.skygrid.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.possible_triangle.skygrid.api.SkygridConstants
import com.possible_triangle.skygrid.api.world.Generator
import com.possible_triangle.skygrid.api.world.IBlockAccess
import com.possible_triangle.skygrid.api.xml.elements.DimensionConfig
import com.possible_triangle.skygrid.api.xml.elements.Distance
import com.possible_triangle.skygrid.world.BlockAccess
import com.possible_triangle.skygrid.xml.XMLResource
import com.possible_triangle.skygrid.xml.resources.DimensionConfigs
import com.possible_triangle.skygrid.xml.resources.Presets
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.commands.arguments.blocks.BlockInput
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
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

    private val AIR = Blocks.AIR.defaultBlockState()

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
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

        val range = { gen: (CommandContext<CommandSourceStack>) -> DimensionConfig ->
            val executor: Command<CommandSourceStack> = Command { generateRange(it, gen(it)) }
            val range = argument("end", BlockPosArgument.blockPos()).executes(executor).then(

                argument("snap", BoolArgumentType.bool()).executes(executor).then(
                    literal("random").executes(executor)
                        .then(argument("distance", IntegerArgumentType.integer(1)).executes(executor))
                )
                    .then(
                        argument("seed", LongArgumentType.longArg()).executes(executor)
                            .then(argument("distance", IntegerArgumentType.integer(1)).executes(executor))
                    )

            )
            singleBlock(gen).then(range)
        }

        dispatcher.register(
            literal(SkygridConstants.MOD_ID).then(
                literal("generate")
                    .then(
                        literal("preset").then(
                            resourceArgument("preset", Presets).then(singleBlock {
                                val key = ResourceLocationArgument.getId(it, "preset")
                                Presets[key] ?: throw UNKNOWN_PRESET.create(key)
                            })
                        )
                    )
                    .then(resourceArgument("config", DimensionConfigs).then(range {
                        val key = ResourceLocationArgument.getId(it, "config")
                        DimensionConfigs[key] ?: throw UNKNOWN_CONFIG.create(key)
                    }))
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
        generator: DimensionConfig,
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

}