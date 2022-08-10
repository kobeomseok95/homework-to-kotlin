package com.triple.homework.event.domain

enum class PointType(
    val reason: String,
    val point: Int,
) {

    FIRST_REVIEW_AT_PLACE("해당 장소의 첫 번째 리뷰", 1),
    EXIST_CONTENT("1자 이상의 텍스트 작성", 1),
    EXIST_PHOTO("1장 이상의 사진 첨부", 1),

    REMOVE_FIRST_REVIEW_AT_PLACE("해당 장소의 첫 번째 리뷰 삭제로 인한 점수 감소", -1),
    REMOVE_CONTENT("텍스트 삭제로 인한 점수 감소", -1),
    REMOVE_PHOTO("첨부된 사진의 삭제로 인한 점수 감소", -1),
    ;
}