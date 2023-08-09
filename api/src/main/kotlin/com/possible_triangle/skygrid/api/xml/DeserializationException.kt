package com.possible_triangle.skygrid.api.xml

import kotlinx.serialization.SerializationException

class DeserializationException(
    val location: String?,
    val field: String,
    val candidates: Collection<Any> = emptyList(),
) :
    SerializationException("Unknown field $field at $location")