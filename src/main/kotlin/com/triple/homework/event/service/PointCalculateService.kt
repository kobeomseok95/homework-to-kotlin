package com.triple.homework.event.service

import com.triple.homework.event.domain.PointHistory
import com.triple.homework.event.domain.PointType
import com.triple.homework.event.domain.Review
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class PointCalculateService(
    private val reviewRepository: ReviewRepository,
) {

    fun calculateAddReviewPoint(requestDto: ReviewRequestDto): List<PointHistory> {
        val pointTypes = mutableListOf<PointType>()
        if (hasContent(requestDto.content)) {
            pointTypes.add(PointType.EXIST_CONTENT)
        }
        if (havePhotos(requestDto.attachedPhotoIds)) {
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

    private fun hasContent(content: String?) =
        !content.isNullOrBlank()

    private fun havePhotos(attachedPhotoIds: List<UUID>) =
        attachedPhotoIds.isNotEmpty()

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
        if (review.content == requestDto.content) {
            return
        }

        if (review.content.isNullOrBlank() && !requestDto.content.isNullOrBlank()) {
            pointTypes.add(PointType.EXIST_CONTENT)
        } else if (!review.content.isNullOrBlank() && requestDto.content.isNullOrBlank()) {
            pointTypes.add(PointType.REMOVE_CONTENT)
        }
    }

    private fun checkChangedAttachedPhotos(pointTypes: MutableList<PointType>,
                                           review: Review,
                                           requestDto: ReviewRequestDto,
    ) {
        if (review.photosSize == requestDto.attachedPhotoIds.size) {
            return
        }

        if (!review.havePhotos && requestDto.attachedPhotoIds.isNotEmpty()) {
            pointTypes.add(PointType.EXIST_PHOTO)
        } else if (review.havePhotos && requestDto.attachedPhotoIds.isEmpty()) {
            pointTypes.add(PointType.REMOVE_PHOTO)
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
}