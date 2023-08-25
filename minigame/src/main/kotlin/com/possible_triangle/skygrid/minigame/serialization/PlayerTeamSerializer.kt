package com.possible_triangle.skygrid.minigame.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.server.MinecraftServer
import net.minecraft.world.scores.PlayerTeam

class PlayerTeamSerializer(private val server: MinecraftServer) : KSerializer<PlayerTeam> {

    override val descriptor = PrimitiveSerialDescriptor("PlayerTeam", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PlayerTeam {
        val name = decoder.decodeString()
        return server.scoreboard.getPlayerTeam(name)
            ?: throw IllegalArgumentException("PlayerTeam $name does not exist")
    }

    override fun serialize(encoder: Encoder, value: PlayerTeam) {
        encoder.encodeString(value.name)
    }
}