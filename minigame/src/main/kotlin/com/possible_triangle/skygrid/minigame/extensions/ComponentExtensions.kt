package com.possible_triangle.skygrid.minigame.extensions

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

operator fun MutableComponent.plus(other: MutableComponent): MutableComponent = append(other)

operator fun MutableComponent.plus(other: String): MutableComponent = append(other)

infix fun MutableComponent.append(other: MutableComponent): MutableComponent = plus(" ") + other

infix fun MutableComponent.append(other: String): MutableComponent = plus(" $other")

infix fun String.append(other: MutableComponent): MutableComponent = Component.literal("$this ") + other