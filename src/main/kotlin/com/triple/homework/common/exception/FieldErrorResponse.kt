package com.triple.homework.common.exception

data class FieldErrorResponse (

    val field: String?,
    val value: String?,
    val reason: String?,
)