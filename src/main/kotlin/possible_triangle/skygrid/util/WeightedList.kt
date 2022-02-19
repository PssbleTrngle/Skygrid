package possible_triangle.skygrid.util

import possible_triangle.skygrid.config.WeightedEntry
import java.util.*
import kotlin.random.Random

class WeightedList<T : WeightedEntry>(pairs: List<T>) {

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

    fun random(random: Random): T {
        if (isEmpty()) throw IllegalArgumentException("WeightedList is empty")
        val value = random.nextDouble() * total
        return map.higherEntry(value)?.value ?: throw NullPointerException("No entry found")
    }

    val size: Int
        get() = map.size

    fun isEmpty(): Boolean {
        return map.isEmpty() || total == 0.0
    }

    fun isNotEmpty(): Boolean {
        return map.isNotEmpty() && total > 0.0
    }

}