package com.possible_triangle.skygrid.api.xml.elements

import com.possible_triangle.skygrid.api.WeightedList
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random

@Serializable
data class ListWrapper<T : WeightedEntry>(
    private val children: List<T> = listOf(),
    private val replace: Boolean = false,
): Collection<T> {

    @Transient
    private lateinit var weighted: WeightedList<T>

    constructor(vararg children: T) : this(children.toList())

    operator fun plus(other: ListWrapper<T>): ListWrapper<T> {
        if (other.replace) return other
        return ListWrapper(children + other.children)
    }

    fun validate(predicate: (T) -> Boolean): Boolean {
        weighted = WeightedList(children.filter(predicate))
        return weighted.isNotEmpty()
    }

    fun isValid(): Boolean {
        return weighted.isNotEmpty()
    }

    fun random(random: Random): T {
        return weighted.random(random)
    }

    override val size: Int
        get() = weighted.size

    override fun contains(element: T): Boolean {
        return weighted.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return weighted.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return weighted.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return weighted.iterator()
    }
}