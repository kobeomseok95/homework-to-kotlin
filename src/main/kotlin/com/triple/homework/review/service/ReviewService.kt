package com.triple.homework.review.service

import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.review.domain.ReviewRepository
import com.triple.homework.review.service.dto.request.ReviewRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewPointCalculateService: ReviewPointCalculateService,
) {

    @Transactional
    fun add(requestDto: ReviewRequestDto) {
        validateExistReview(requestDto)
        val calculatedPoint = reviewPointCalculateService.writeReviewAndSaveHistory(requestDto)
        reviewRepository.save(requestDto.toReview())
    }

    private fun validateExistReview(requestDto: ReviewRequestDto) {
        if (reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)) {
            throw UserWrittenReviewException()
        }
        if (reviewRepository.existsByReviewId(requestDto.reviewId)) {
            throw UserWrittenReviewException()
        }
    }
}