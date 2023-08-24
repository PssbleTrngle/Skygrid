package com.possible_triangle.skygrid.minigame.extensions

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

fun <T : ArgumentBuilder<CommandSourceStack, T>, R> ArgumentBuilder<CommandSourceStack, T>.argument(
    key: String,
    type: ArgumentType<R>,
    block: RequiredArgumentBuilder<CommandSourceStack, R>.() -> ArgumentBuilder<CommandSourceStack, *>,
): T {
    return then(Commands.argument(key, type).let(block))
}


fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.literal(
    value: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> ArgumentBuilder<CommandSourceStack, *>,
): T {
    return then(Commands.literal(value).let(block))
}