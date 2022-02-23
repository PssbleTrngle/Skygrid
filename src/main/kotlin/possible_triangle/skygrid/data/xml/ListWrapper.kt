package possible_triangle.skygrid.data.xml

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import possible_triangle.skygrid.util.WeightedList
import kotlin.random.Random

@Serializable
data class ListWrapper<T : WeightedEntry>(val children: List<T> = listOf()) {

    @Transient
    private lateinit var weighted: WeightedList<T>

    constructor(vararg children: T) : this(children.toList())

    operator fun plus(other: ListWrapper<T>): ListWrapper<T> {
        return ListWrapper(children + other.children)
    }

    fun validate(predicate: (T) -> Boolean): Boolean {
        weighted = WeightedList(children.filter(predicate))
        return weighted.isNotEmpty()
    }

    fun random(random: Random): T {
        return weighted.random(random)
    }

}