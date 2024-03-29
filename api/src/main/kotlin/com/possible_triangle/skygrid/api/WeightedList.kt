package com.possible_triangle.skygrid.api

import com.possible_triangle.skygrid.api.xml.elements.WeightedEntry
import net.minecraft.util.RandomSource
import java.util.*

class WeightedList<T : WeightedEntry>(pairs: List<T>): Collection<T> {

    private val map: NavigableMap<Double, T> = TreeMap()
    private var total = 0.0

    init {
        pairs.forEach {
            add(it, it.weight)
        }
    }

    fun add(element: T, weight: Double) {
        if (weight <= 0.0) return
        total += weight
        map[total] = element
    }

    fun random(random: RandomSource): T {
        if (isEmpty()) throw IllegalArgumentException("WeightedList is empty")
        val value = random.nextDouble() * total
        return map.higherEntry(value)?.value ?: throw NullPointerException("No entry found")
    }

    override  val size: Int
        get() = map.size

   override fun isEmpty(): Boolean {
        return map.isEmpty() || total == 0.0
    }

    override fun contains(element: T): Boolean = map.values.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = map.values.containsAll(elements)

    override fun iterator(): Iterator<T> = map.values.iterator()

}