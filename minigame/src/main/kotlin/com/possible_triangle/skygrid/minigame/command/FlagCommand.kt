package com.possible_triangle.skygrid.minigame.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.possible_triangle.skygrid.minigame.extensions.append
import com.possible_triangle.skygrid.minigame.extensions.argument
import com.possible_triangle.skygrid.minigame.extensions.literal
import com.possible_triangle.skygrid.minigame.extensions.participantTeams
import com.possible_triangle.skygrid.minigame.gamedata.Flags
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.TeamArgument
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component

object FlagCommand {

    fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.flagCommandNode(): T {
        return run {
            literal("place") {
                argument("team", TeamArgument.team()) {
                    executes(::placeFlag)
                }
            }

            literal("remove") {
                argument("team", TeamArgument.team()) {
                    executes(::removeFlag)
                }

                literal("*") {
                    executes(::removeFlags)
                }
            }

            literal("show") {
                argument("team", TeamArgument.team()) {
                    executes(::showFlag)
                }
            }
        }
    }

    private fun placeFlag(ctx: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(ctx, "team")
        val pos = BlockPos(ctx.source.position)

        Flags.place(ctx.source.server, team, pos)

        ctx.source.sendSuccess("Placed Flag for" append team.formattedDisplayName, true)
        return 1
    }

    private fun showFlag(ctx: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(ctx, "team")

        val pos = Flags.get(ctx.source.server, team)

        return if (pos != null) {
            ctx.source.sendSuccess(
                "Flag of" append team.formattedDisplayName append "is at",
                true
            )
            1
        } else {
            ctx.source.sendFailure("No flag found for" append team.formattedDisplayName)
            0
        }
    }

    private fun removeFlag(ctx: CommandContext<CommandSourceStack>): Int {
        val team = TeamArgument.getTeam(ctx, "team")

        Flags.remove(ctx.source.server, team)

        ctx.source.sendSuccess("Removed Flag of" append team.formattedDisplayName, true)
        return 1
    }

    private fun removeFlags(ctx: CommandContext<CommandSourceStack>): Int {
        val teams = ctx.source.server.participantTeams

        teams.forEach {
            Flags.remove(ctx.source.server, it)
        }

        ctx.source.sendSuccess(Component.literal("Removed flags of ${teams.size} teams"), true)
        return teams.size
    }

}