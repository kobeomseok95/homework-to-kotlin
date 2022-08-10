package com.triple.homework.event.service

import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.event.domain.PointHistoryRepository
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewEventService(
    private val reviewRepository: ReviewRepository,
    private val pointCalculateService: PointCalculateService,
    private val pointHistoryRepository: PointHistoryRepository,
    private val userPointService: UserPointService,
) {

    fun add(requestDto: ReviewRequestDto) {
        validateExistReview(requestDto)
        val reviewPoints = pointCalculateService.calculateAddReviewPoint(requestDto)
        reviewRepository.save(requestDto.toReview())
        pointHistoryRepository.saveAll(reviewPoints)
        userPointService.saveUserIfNotFoundAndCalculateUserPoint(
            requestDto.userId,
            reviewPoints.sumOf { it.pointType.point },
        )
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