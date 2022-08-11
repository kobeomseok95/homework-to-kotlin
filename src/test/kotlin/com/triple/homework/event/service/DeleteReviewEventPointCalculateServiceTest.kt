package com.triple.homework.event.service

import com.triple.homework.event.domain.*
import io.mockk.mockk
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteReviewEventPointCalculateServiceTest {

    private val reviewRepository: ReviewRepository = mockk(relaxed = true)
    private val pointCalculateService: PointCalculateService = PointCalculateService(
        reviewRepository,
    )

    @DisplayName("리뷰 점수 계산 - 첫 리뷰, 내용 존재, 사진이 있다면 3점 회수")
    @Test
    fun point_calculate_delete_first_review_exist_content_exist_photos() {
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

        val reviewPoints = pointCalculateService.calculateDeleteReviewPoint(review)

        assertThat(reviewPoints).hasSize(3)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_CONTENT,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_PHOTO,
                ),
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_FIRST_REVIEW_AT_PLACE,
                ),
            ))
    }

    @DisplayName("리뷰 점수 계산 - 첫 리뷰를 남긴 상황만일 경우 1점 회수")
    @Test
    fun point_calculate_delete_first_review() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = true,
            attachedPhotos = AttachedPhotos(),
        )

        val reviewPoints = pointCalculateService.calculateDeleteReviewPoint(review)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_FIRST_REVIEW_AT_PLACE,
                ),
            ))
    }

    @DisplayName("리뷰 점수 계산 - 내용만 작성된 경우 1점 회수")
    @Test
    fun point_calculate_delete_exist_content() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(),
        )

        val reviewPoints = pointCalculateService.calculateDeleteReviewPoint(review)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_CONTENT,
                ),
            ))
    }

    @DisplayName("리뷰 점수 계산 - 첨부사진만 존재하는 경우 1점 회수")
    @Test
    fun point_calculate_delete_exist_photos() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            attachedPhotos = AttachedPhotos(mutableSetOf(
                AttachedPhoto(UUID.randomUUID())
            )),
            isFirstReview = false,
        )

        val reviewPoints = pointCalculateService.calculateDeleteReviewPoint(review)

        assertThat(reviewPoints).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(listOf(
                PointHistory(
                    userId = review.userId,
                    pointType = PointType.REMOVE_PHOTO,
                ),
            ))
    }
}
