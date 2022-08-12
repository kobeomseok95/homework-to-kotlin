package com.triple.homework.common.exception

enum class ClientErrorCode(

    private val code: Int,
    private val message: String,
): ErrorEnumCode {

    INVALID_REQUEST(1001, "입력값에 대한 예외입니다."),
    ;

    override fun getCode() = code

    override fun getMessage() = message
}
