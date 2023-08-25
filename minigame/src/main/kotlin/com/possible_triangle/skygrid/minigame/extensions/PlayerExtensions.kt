package com.possible_triangle.skygrid.minigame.extensions

import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.scores.PlayerTeam

val Player.participantTeam get(): PlayerTeam? = team.takeIf { it is PlayerTeam } as PlayerTeam?

val Player.isParticipant get() = participantTeam != null

val MinecraftServer.participantTeams: Collection<PlayerTeam> get() = scoreboard.playerTeams

val MinecraftServer.data get() = overworld().dataStorage