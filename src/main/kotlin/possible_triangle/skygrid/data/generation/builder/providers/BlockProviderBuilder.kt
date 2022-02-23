package possible_triangle.skygrid.data.generation.builder

import possible_triangle.skygrid.data.xml.BlockProvider
import possible_triangle.skygrid.data.xml.Extra
import possible_triangle.skygrid.data.xml.Transformer

open class BlockProviderBuilder<T : BlockProvider> {

    private val extras = arrayListOf<Extra>()
    private val transformers = arrayListOf<Transformer>()
    var weight: Double = 1.0

    fun build(builder: (Double, List<Extra>, List<Transformer>) -> T): T {
        return builder(weight, extras.toList(), transformers.toList())
    }

}