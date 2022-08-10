package com.triple.homework.event.service

import com.triple.homework.event.domain.PointHistory
import com.triple.homework.event.domain.PointType
import com.triple.homework.event.domain.ReviewRepository
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class PointCalculateServiceTest {

    private val reviewRepository: ReviewRepository = mockk()
    private val pointCalculateService: PointCalculateService = PointCalculateService(
        reviewRepository,
    )

    @DisplayName("리뷰 점수 계산 - 첫 리뷰가 아닌 경우, 컨텐츠가 없는 경우, 사진이 없는 경우")
    @Test
    fun point_calculate_not_first_review_not_exist_contents_not_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories).hasSize(0)
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰가 아닌 경우, 컨텐츠가 있는 경우, 사진이 없는 경우")
    @Test
    fun point_calculate_not_first_review_exist_contents_not_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
            content = "테스트 컨텐츠",
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(1)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_CONTENT,
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰가 아닌 경우, 컨텐츠가 없는 경우, 사진이 있는 경우")
    @Test
    fun point_calculate_not_first_review_not_exist_contents_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(
                UUID.randomUUID()
            ),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories).hasSize(1)
        assertThat(pointHistories[0].userId).isEqualTo(requestDto.userId)
        assertThat(pointHistories[0].pointType).isEqualTo(PointType.EXIST_PHOTO)
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰가 아닌 경우, 컨텐츠가 있는 경우, 사진이 있는 경우")
    @Test
    fun point_calculate_not_first_review_exist_contents_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(
                UUID.randomUUID()
            ),
            content = "테스트 컨텐츠",
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(2)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_PHOTO
                ),
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_CONTENT
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰인 경우, 컨텐츠가 없는 경우, 사진이 없는 경우")
    @Test
    fun point_calculate_first_review_not_exist_contents_not_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(1)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰인 경우, 컨텐츠가 있는 경우, 사진이 없는 경우")
    @Test
    fun point_calculate_first_review_exist_contents_not_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
            content = "테스트 컨텐츠",
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(2)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE
                ),
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_CONTENT
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰인 경우, 컨텐츠가 없는 경우, 사진이 있는 경우")
    @Test
    fun point_calculate_first_review_not_exist_contents_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(
                UUID.randomUUID()
            ),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(2)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE
                ),
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_PHOTO
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰인 경우, 컨텐츠가 있는 경우, 사진이 있는 경우")
    @Test
    fun point_calculate_first_review_exist_contents_exist_photos() {

        val requestDto = ReviewRequestDto(
            reviewId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            attachedPhotoIds = listOf(
                UUID.randomUUID()
            ),
            content = "테스트 컨텐츠",
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val pointHistories = pointCalculateService.calculateAddReviewPoint(requestDto)

        assertThat(pointHistories)
            .hasSize(3)
        assertThat(pointHistories)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE
                ),
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_PHOTO
                ),
                PointHistory(
                    userId = requestDto.userId,
                    pointType = PointType.EXIST_CONTENT
                ),
            ))
        verify {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        }
    }
}
