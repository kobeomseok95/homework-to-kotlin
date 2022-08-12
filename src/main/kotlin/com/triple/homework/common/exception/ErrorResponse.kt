package com.triple.homework.common.exception

import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import java.time.LocalDateTime

data class ErrorResponse(

    val time: LocalDateTime = LocalDateTime.now(),
    val message: String?,
    val status: Int = HttpStatus.BAD_REQUEST.value(),
    val code: Int,
    val errors: List<FieldErrorResponse> = listOf(),
) {
    constructor(e: ApplicationException): this(
        message = e.message,
        code = e.errorEnumCode.getCode()
    )

    constructor(bindingResult: BindingResult): this(
        message = ClientErrorCode.INVALID_REQUEST.getMessage(),
        code = ClientErrorCode.INVALID_REQUEST.getCode(),
        errors = bindingResult.fieldErrors
            .map { FieldErrorResponse(
                field = it.field,
                value = it.rejectedValue?.toString(),
                reason = it.defaultMessage,
            ) }.toList()
    )
}
