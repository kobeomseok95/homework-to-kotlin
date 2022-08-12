package com.triple.homework.common.exception

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun applicationException(e: ApplicationException) =
        ErrorResponse(e)

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun applicationException(e: BindException) =
        ErrorResponse(e.bindingResult)
}