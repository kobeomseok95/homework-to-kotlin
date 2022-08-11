package com.triple.homework.event.service

import com.triple.homework.event.domain.*
import com.triple.homework.event.service.dto.request.ReviewRequestDto
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class ModifyReviewEventPointCalculateServiceTest {

    private val reviewRepository: ReviewRepository = mockk(relaxed = true)
    private val pointCalculateService: PointCalculateService = PointCalculateService(
        reviewRepository,
    )

    @DisplayName("리뷰 점수 계산 - 내용이 있던 리뷰에서 내용이 없는 리뷰로 변경된다면 1점 회수")
    @Test
    fun point_calculate_modify_review_exist_content_to_not_exist_content() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            isFirstReview = true,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = review.placeId,
            attachedPhotoIds = listOf(),
        )

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_CONTENT,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 내용이 없던 리뷰에서 내용이 추가되면 1점 추가")
    @Test
    fun point_calculate_modify_review_not_exist_content_to_exist_content() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = true,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = review.placeId,
            attachedPhotoIds = listOf(),
            content = "테스트 컨텐츠",
        )

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.EXIST_CONTENT,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 첨부사진이 없던 리뷰에서 첨부사진이 추가되면 1점 추가")
    @Test
    fun point_calculate_modify_review_not_exist_photo_to_exist_photo() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = true,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = review.placeId,
            attachedPhotoIds = listOf(UUID.randomUUID()),
        )

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.EXIST_PHOTO,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 첨부사진이 있던 리뷰에서 첨부사진이 삭제되면 1점 회수")
    @Test
    fun point_calculate_modify_review_exist_photo_to_not_exist_photo() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotos = AttachedPhotos(mutableSetOf(
                AttachedPhoto(UUID.randomUUID())
            )),
            isFirstReview = true,
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = review.placeId,
            attachedPhotoIds = listOf(),
        )

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_PHOTO,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 기존 리뷰가 첫 리뷰인데 장소가 변경될 때 첫 리뷰가 아니면 1점 회수")
    @Test
    fun point_calculate_modify_review_changed_place_is_not_first_review() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = true,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_FIRST_REVIEW_AT_PLACE,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 기존 장소가 첫 리뷰가 아닌데 장소가 변경될 때 첫 리뷰라면 1점 추가")
    @Test
    fun point_calculate_modify_review_changed_place_is_first_review() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE,
                )
            ))
    }

    @DisplayName("리뷰 점수 계산 - 변경된 장소가 첫 리뷰, 내용 작성, 첨부사진 추가 시 3점 추가")
    @Test
    fun point_calculate_modify_review_changed_place_is_first_review_and_exist_content_and_exist_photo() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(),
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(UUID.randomUUID()),
            content = "테스트 컨텐츠",
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns false

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(3)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.FIRST_REVIEW_AT_PLACE,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.EXIST_PHOTO,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.EXIST_CONTENT,
                ),
            ))
    }

    @DisplayName("리뷰 점수 계산 - 변경된 장소가 이미 작성된 리뷰가 있음, 내용 삭제, 첨부사진 삭제 시 3점 회수")
    @Test
    fun point_calculate_modify_review_changed_place_is_not_first_review_and_not_exist_content_and_not_exist_photo() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotos = AttachedPhotos(mutableSetOf(
                AttachedPhoto(UUID.randomUUID())
            )),
            content = "테스트 컨텐츠",
            isFirstReview = true,
        )
        val requestDto = ReviewRequestDto(
            reviewId = review.id,
            userId = review.userId,
            placeId = UUID.randomUUID(),
            attachedPhotoIds = listOf(),
        )
        every {
            reviewRepository.existsByPlaceId(requestDto.placeId)
        } returns true

        val reviewPoints = pointCalculateService.calculateModifyReviewPoint(review, requestDto)

        assertThat(reviewPoints).hasSize(3)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_FIRST_REVIEW_AT_PLACE,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_PHOTO,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_CONTENT,
                ),
            ))
    }
}
