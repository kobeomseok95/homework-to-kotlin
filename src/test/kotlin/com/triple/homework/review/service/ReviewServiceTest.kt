package com.triple.homework.review.service

import com.triple.homework.common.exception.review.UserWrittenReviewException
import com.triple.homework.review.domain.Review
import com.triple.homework.review.domain.ReviewRepository
import com.triple.homework.review.service.dto.request.ReviewRequestDto
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class ReviewServiceTest {

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @MockK
    private lateinit var reviewPointCalculateService: ReviewPointCalculateService

    @InjectMockKs
    private lateinit var reviewService: ReviewService

    @DisplayName("리뷰 작성 - 실패 / 유저가 이미 작성한 리뷰인 경우 추가할 수 없다.")
    @Test
    fun review_add_fail_exists_by_userId_and_placeId() {

        every {
            reviewRepository.existsByUserIdAndPlaceId(any(), any())
        } returns true
        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID().toString()),
            content = "테스트",
        )

        assertThrows<UserWrittenReviewException> {
            reviewService.add(requestDto)
        }
        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
        }
    }

    @DisplayName("리뷰 작성 - 실패 / 이미 작성된 리뷰 ID가 있을 경우 작성할 수 없다.")
    @Test
    fun review_add_fail_exists_by_reviewId() {

        every {
            reviewRepository.existsByUserIdAndPlaceId(any(), any())
        } returns false
        every {
            reviewRepository.existsByReviewId(any())
        } returns true
        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID().toString()),
            content = "테스트",
        )

        assertThrows<UserWrittenReviewException> {
            reviewService.add(requestDto)
        }
        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
            reviewRepository.existsByReviewId(requestDto.reviewId)
        }
    }

    @DisplayName("리뷰 생성 - 성공")
    @Test
    fun review_add_success() {
        every {
            reviewRepository.existsByUserIdAndPlaceId(any(), any())
        } returns false
        every {
            reviewRepository.existsByReviewId(any())
        } returns false
        every {
            reviewRepository.save(any())
        } returns mockkClass(Review::class)
        every {
            reviewPointCalculateService.writeReviewAndSaveHistory(any())
        } returns 3
        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID().toString()),
            content = "테스트",
        )

        reviewService.add(requestDto)

        verify {
            reviewRepository.existsByUserIdAndPlaceId(requestDto.userId, requestDto.placeId)
            reviewRepository.existsByReviewId(requestDto.reviewId)
            reviewRepository.save(any())
            reviewPointCalculateService.writeReviewAndSaveHistory(requestDto)
        }
    }
}
