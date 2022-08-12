package com.triple.homework.common.exception

open class ApplicationException(
    val errorEnumCode: ErrorEnumCode,
): RuntimeException(errorEnumCode.getMessage())