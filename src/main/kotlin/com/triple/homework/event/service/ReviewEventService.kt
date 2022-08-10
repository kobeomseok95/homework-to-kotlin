package com.triple.homework.event.service

import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewEventService(
    private val reviewRepository: ReviewRepository,
    private val pointCalculateService: PointCalculateService,
) {

    fun add(requestDto: ReviewRequestDto) {
        validateExistReview(requestDto)
        pointCalculateService.calculateAddReviewPoint(requestDto)
        val review = requestDto.toReview()
        reviewRepository.save(review)
    }

    private fun validateExistReview(requestDto: ReviewRequestDto) {
        if (reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)) {
            throw UserWrittenReviewException()
        }
        if (reviewRepository.existsById(requestDto.reviewId)) {
            throw UserWrittenReviewException()
        }
    }
}