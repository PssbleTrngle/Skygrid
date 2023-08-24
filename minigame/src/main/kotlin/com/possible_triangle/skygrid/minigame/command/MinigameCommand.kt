package com.possible_triangle.skygrid.minigame.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.possible_triangle.skygrid.minigame.command.FlagCommand.flagCommandNode
import com.possible_triangle.skygrid.minigame.extensions.literal
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component

object MinigameCommand {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>, ctx: CommandBuildContext) {
        dispatcher.register(
            literal("minigame")
                .literal("start") { executes(::start) }
                .literal("stop") { executes(::stop) }
                .literal("flag") { flagCommandNode() }
        )
    }

    private fun start(ctx: CommandContext<CommandSourceStack>): Int {

        ctx.source.sendSuccess(Component.literal("Started Game"), true)
        return 0
    }

    private fun stop(ctx: CommandContext<CommandSourceStack>): Int {

        ctx.source.sendSuccess(Component.literal("Stopped Game"), true)
        return 0
    }

}
