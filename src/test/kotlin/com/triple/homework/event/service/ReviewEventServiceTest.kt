package com.triple.homework.event.service

import com.triple.homework.common.exception.review.ReviewNotFoundException
import com.triple.homework.common.exception.review.ReviewerNotEqualsException
import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.event.domain.*
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.util.*

internal class ReviewEventServiceTest {

    private val reviewRepository: ReviewRepository = mockk(relaxed = true)
    private val pointCalculateService: PointCalculateService = mockk()
    private val pointHistoryRepository: PointHistoryRepository = mockk()
    private val userPointService: UserPointService = mockk(relaxed = true)
    private val reviewEventService: ReviewEventService = ReviewEventService(
        reviewRepository,
        pointCalculateService,
        pointHistoryRepository,
        userPointService,
    )

    @DisplayName("리뷰 작성 - 실패 / 유저가 이미 작성한 리뷰인 경우 추가할 수 없다.")
    @Test
    fun review_add_fail_exists_by_userId_and_placeId() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        every {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        } returns true

        assertThrows<UserWrittenReviewException> {
            reviewEventService.add(requestDto)
        }
        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        }
    }

    @DisplayName("리뷰 작성 - 실패 / 이미 작성된 리뷰 ID가 있을 경우 작성할 수 없다.")
    @Test
    fun review_add_fail_exists_by_reviewId() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        every {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        } returns false
        every {
            reviewRepository.existsById(requestDto.reviewId)
        } returns true

        assertThrows<UserWrittenReviewException> {
            reviewEventService.add(requestDto)
        }
        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
            reviewRepository.existsById(requestDto.reviewId)
        }
    }

    @DisplayName("리뷰 생성 - 성공 / 첫 리뷰, 내용이 있음, 사진이 있다고 가정")
    @Test
    fun review_add_success() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        every {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        } returns false
        every {
            reviewRepository.existsById(requestDto.reviewId)
        } returns false
        val pointHistories = listOf(
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_CONTENT,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_PHOTO,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.FIRST_REVIEW_AT_PLACE,
            ),
        )
        every {
            pointCalculateService.calculateAddReviewPoint(requestDto)
        } returns pointHistories
        val review = requestDto.toReview(true)
        every {
            reviewRepository.save(any())
        } returns review
        every {
            pointHistoryRepository.saveAll(pointHistories)
        } returns pointHistories

        reviewEventService.add(requestDto)

        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
            reviewRepository.existsById(requestDto.reviewId)
            pointCalculateService.calculateAddReviewPoint(requestDto)
            reviewRepository.save(any())
            userPointService.saveUserIfNotFoundAndCalculateUserPoint(
                requestDto.userId,
                pointHistories.sumOf {
                    it.pointType.point
                }
            )
        }
    }

    @DisplayName("리뷰 수정 - 실패 / 리뷰가 없을 경우")
    @Test
    fun review_modify_fail_review_not_found() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        every {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
        } returns null

        assertThrows<ReviewNotFoundException> {
            reviewEventService.modify(requestDto)
        }
        verify {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
        }
    }

    @DisplayName("리뷰 수정 - 실패 / 찾은 리뷰의 작성자가 맞지 않을 경우 예외 발생")
    @Test
    fun review_modify_fail_not_equals_reviewer() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        val review = Review(
            id = requestDto.reviewId,
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(),
        )
        every {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
        } returns review
        val pointHistories = listOf(
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_CONTENT,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_PHOTO,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.FIRST_REVIEW_AT_PLACE,
            ),
        )
        every {
            pointCalculateService.calculateModifyReviewPoint(review, requestDto)
        } returns pointHistories

        assertThrows<ReviewerNotEqualsException> {
            reviewEventService.modify(requestDto)
        }
        verify {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
            pointCalculateService.calculateModifyReviewPoint(review, requestDto)
        }
    }

    @DisplayName("리뷰 수정 - 성공 / 0점에서 3점으로 변화한다고 가정")
    @Test
    fun review_modify_success() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        val review = Review(
            id = requestDto.reviewId,
            userId = requestDto.userId,
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(),
        )
        every {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
        } returns review
        val pointHistories = listOf(
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_CONTENT,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.EXIST_PHOTO,
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.FIRST_REVIEW_AT_PLACE,
            ),
        )
        every {
            pointCalculateService.calculateModifyReviewPoint(review, requestDto)
        } returns pointHistories
        every {
            pointHistoryRepository.saveAll(pointHistories)
        } returns pointHistories

        reviewEventService.modify(requestDto)

        verify {
            reviewRepository.findWithAttachedPhotosById(requestDto.reviewId)
            pointCalculateService.calculateModifyReviewPoint(review, requestDto)
            pointHistoryRepository.saveAll(pointHistories)
            userPointService.changeUserPoint(requestDto.userId, pointHistories.sumOf { it.pointType.point })
        }
        assertThat(review)
            .usingRecursiveComparison()
            .isEqualTo(requestDto.toReview(true))
    }

    @DisplayName("리뷰 삭제 - 실패 / 리뷰가 없을 경우")
    @Test
    fun delete_review_fail_not_found_review() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        every {
            reviewRepository.findByIdOrNull(requestDto.reviewId)
        } returns null

        assertThrows<ReviewNotFoundException> {
            reviewEventService.delete(requestDto)
        }
    }

    @DisplayName("리뷰 삭제 - 성공 / 첫 리뷰, 내용 작성, 첨부파일이 존재한다고 가정")
    @Test
    fun delete_review_success() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트",
        )
        val review = requestDto.toReview(true)
        every {
            reviewRepository.findByIdOrNull(requestDto.reviewId)
        } returns review
        val pointHistories = listOf(
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.REMOVE_FIRST_REVIEW_AT_PLACE
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.REMOVE_PHOTO
            ),
            PointHistory(
                userId = requestDto.userId,
                pointType = PointType.REMOVE_CONTENT
            ),
        )
        every {
            pointCalculateService.calculateDeleteReviewPoint(review)
        } returns pointHistories
        every {
            pointHistoryRepository.saveAll(pointHistories)
        } returns pointHistories

        reviewEventService.delete(requestDto)

        verify {
            reviewRepository.findByIdOrNull(requestDto.reviewId)
            pointCalculateService.calculateDeleteReviewPoint(review)
            pointHistoryRepository.saveAll(pointHistories)
            userPointService.changeUserPoint(requestDto.userId, pointHistories.sumOf { it.pointType.point })
            reviewRepository.delete(review)
        }
    }
}
