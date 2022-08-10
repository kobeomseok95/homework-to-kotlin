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

    fun calculateModifyReviewPoint(review: Review, requestDto: ReviewRequestDto): List<PointHistory> {
        TODO("Not yet implemented")
        return listOf()
    }
}