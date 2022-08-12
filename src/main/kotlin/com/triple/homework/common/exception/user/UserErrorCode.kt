package com.triple.homework.common.exception.user

import com.triple.homework.common.exception.ErrorEnumCode

enum class UserErrorCode(

    private val code: Int,
    private val message: String,
): ErrorEnumCode {

    NOT_FOUND_USER(1201, "존재하지 않는 유저입니다."),
    ;

    override fun getCode() = code

    override fun getMessage() = message
}