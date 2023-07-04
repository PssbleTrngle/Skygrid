package com.possible_triangle.skygrid.api.xml

import com.possible_triangle.skygrid.api.xml.elements.BlockProvider

interface IReferenceContext {

    operator fun get(key: String): BlockProvider?

    fun with(provider: BlockProvider): IReferenceContext

    fun with(values: Map<String, BlockProvider>): IReferenceContext

}