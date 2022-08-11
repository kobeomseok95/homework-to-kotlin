package com.triple.homework.event.service

import com.triple.homework.event.domain.PointHistory
import com.triple.homework.event.domain.PointType
import com.triple.homework.event.domain.Review
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import org.springframework.stereotype.Service

@Service
class PointCalculateService(
    private val reviewRepository: ReviewRepository,
) {

    fun calculateAddReviewPoint(requestDto: ReviewRequestDto): List<PointHistory> {
        val pointTypes = mutableListOf<PointType>()
        if (requestDto.hasContent) {
            pointTypes.add(PointType.EXIST_CONTENT)
        }
        if (requestDto.havePhotos) {
            pointTypes.add(PointType.EXIST_PHOTO)
        }
        if (isFirstReview(requestDto)) {
            pointTypes.add(PointType.FIRST_REVIEW_AT_PLACE)
        }

        return pointTypes.map {
            PointHistory(
                userId = requestDto.userId,
                pointType = it
            )
        }
    }

    private fun isFirstReview(requestDto: ReviewRequestDto) =
        !reviewRepository.existsByPlaceId(requestDto.placeId)

    fun calculateModifyReviewPoint(review: Review,
                                   requestDto: ReviewRequestDto
    ): List<PointHistory> {
        val pointTypes = mutableListOf<PointType>()
        checkChangedContent(pointTypes, review, requestDto)
        checkChangedAttachedPhotos(pointTypes, review, requestDto)
        checkChangedPlace(pointTypes, review, requestDto)
        return pointTypes.map {
            PointHistory(
                userId = requestDto.userId,
                pointType = it
            )
        }
    }

    private fun checkChangedContent(pointTypes: MutableList<PointType>,
                                    review: Review,
                                    requestDto: ReviewRequestDto,
    ) {
        if (!review.hasContent && requestDto.hasContent) {
            pointTypes.add(PointType.EXIST_CONTENT)
            return
        } else if (review.hasContent && !requestDto.hasContent) {
            pointTypes.add(PointType.REMOVE_CONTENT)
            return
        }
    }

    private fun checkChangedAttachedPhotos(pointTypes: MutableList<PointType>,
                                           review: Review,
                                           requestDto: ReviewRequestDto,
    ) {
        if (!review.havePhotos && requestDto.havePhotos) {
            pointTypes.add(PointType.EXIST_PHOTO)
            return
        } else if (review.havePhotos && !requestDto.havePhotos) {
            pointTypes.add(PointType.REMOVE_PHOTO)
            return
        }
    }

    private fun checkChangedPlace(pointTypes: MutableList<PointType>,
                                  review: Review,
                                  requestDto: ReviewRequestDto,
    ) {
        if (review.placeId == requestDto.placeId) {
            return
        }

        if (review.isFirstReview && !isFirstReview(requestDto)) {
            pointTypes.add(PointType.REMOVE_FIRST_REVIEW_AT_PLACE)
            return
        } else if (!review.isFirstReview && isFirstReview(requestDto)) {
            pointTypes.add(PointType.FIRST_REVIEW_AT_PLACE)
            return
        }
    }

    fun calculateDeleteReviewPoint(review: Review): List<PointHistory> {
        val pointTypes = mutableListOf<PointType>()
        if (review.isFirstReview) {
            pointTypes.add(PointType.REMOVE_FIRST_REVIEW_AT_PLACE)
        }
        if (review.havePhotos) {
            pointTypes.add(PointType.REMOVE_PHOTO)
        }
        if (review.hasContent) {
            pointTypes.add(PointType.REMOVE_CONTENT)
        }
        return pointTypes.map {
            PointHistory(
                userId = review.userId,
                pointType = it
            )
        }
    }
}