package com.possible_triangle.skygrid.extensions

import net.minecraft.Util
import java.util.concurrent.CompletableFuture

fun <K, V> Map<out K, V>.supplyAsync(transform: (Map.Entry<K, V>) -> Unit): CompletableFuture<*> {
    return CompletableFuture.allOf(
        *map {
            CompletableFuture.supplyAsync(
                { transform(it) },
                Util.backgroundExecutor()
            )
        }.toTypedArray()
    )
}