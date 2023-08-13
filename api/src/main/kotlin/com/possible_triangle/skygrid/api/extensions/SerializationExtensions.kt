package com.possible_triangle.skygrid.api.extensions


import kotlinx.serialization.SerialName

val Any.serialType: String
    get() = with(javaClass) {
        if (isAnnotationPresent(SerialName::class.java)) {
            getAnnotation(SerialName::class.java).value
        } else {
            javaClass.name
        }
    }