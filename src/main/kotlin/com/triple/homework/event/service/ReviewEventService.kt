package com.triple.homework.event.service

import com.triple.homework.common.exception.review.ReviewNotFoundException
import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.event.domain.PointHistoryRepository
import com.triple.homework.event.domain.PointType
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import org.springframework.data.repository.findByIdOrNull
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
        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)
        val isFirstReview = pointHistories.find {
            it.pointType == PointType.FIRST_REVIEW_AT_PLACE
        } != null
        reviewRepository.save(requestDto.toReview(isFirstReview))
        pointHistoryRepository.saveAll(pointHistories)
        userPointService.saveUserIfNotFoundAndCalculateUserPoint(
            requestDto.userId,
            pointHistories.sumOf { it.pointType.point },
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

    fun modify(requestDto: ReviewRequestDto) {
        val review = reviewRepository.findByIdOrNull(requestDto.reviewId)
            ?: throw ReviewNotFoundException()
        val pointHistories = pointCalculateService.calculateModifyReviewPoint(review, requestDto)
        review.update(
            requestDto.userId,
            requestDto.placeId,
            requestDto.content,
            requestDto.toAttachedPhotos(),
        )
        pointHistoryRepository.saveAll(pointHistories)
        userPointService.changeUserPoint(
            requestDto.userId,
            pointHistories.sumOf { it.pointType.point }
        )
    }

    fun delete(requestDto: ReviewRequestDto) {
        val review = reviewRepository.findByIdOrNull(requestDto.reviewId)
            ?: throw ReviewNotFoundException()
        val pointHistories = pointCalculateService.calculateDeleteReviewPoint(review)
        pointHistoryRepository.saveAll(pointHistories)
        userPointService.changeUserPoint(
            requestDto.userId,
            pointHistories.sumOf { it.pointType.point }
        )
    }
}