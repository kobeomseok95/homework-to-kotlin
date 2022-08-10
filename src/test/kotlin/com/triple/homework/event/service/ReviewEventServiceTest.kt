package com.triple.homework.event.service

import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.event.domain.PointHistory
import com.triple.homework.event.domain.PointType
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class ReviewEventServiceTest {

    private val reviewRepository: ReviewRepository = mockk(relaxed = true, relaxUnitFun = true)
    private val pointCalculateService: PointCalculateService = mockk()
    private val reviewEventService: ReviewEventService = ReviewEventService(
        reviewRepository,
        pointCalculateService,
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

    // TODO: ClassCastException 발생 테스트 코드
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
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        } returns false
        every {
            reviewRepository.existsById(requestDto.reviewId)
        } returns false
        every {
            pointCalculateService.calculateAddReviewPoint(requestDto)
        } returns pointHistories
        every {
            reviewRepository.save(requestDto.toReview())
        } returns requestDto.toReview()

        reviewEventService.add(requestDto)

        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
            reviewRepository.existsById(requestDto.reviewId)
            pointCalculateService.calculateAddReviewPoint(requestDto)
        }
    }
}
