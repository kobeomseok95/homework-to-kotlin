package com.triple.homework.common.exception.review

import com.triple.homework.common.exception.ErrorEnumCode

enum class ReviewErrorCode(

    private val code: Int,
    private val message: String,
): ErrorEnumCode {

    REVIEW_NOT_FOUND(1101, "리뷰를 찾을 수 없습니다."),
    REVIEWER_NOT_EQUAL(1102, "리뷰 작성자의 ID가 일치하지 않습니다."),
    WRITTEN_REVIEW(1103, "이미 작성된 리뷰입니다."),
    ;

    override fun getCode() = code

    override fun getMessage() = message
}