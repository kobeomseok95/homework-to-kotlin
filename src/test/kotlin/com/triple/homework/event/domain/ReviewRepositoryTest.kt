package com.triple.homework.event.domain

import com.triple.homework.DataJpaTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class ReviewRepositoryTest @Autowired constructor(
    private val reviewRepository: ReviewRepository,
): DataJpaTest() {

    @DisplayName("유저 ID, 장소 ID로 리뷰 찾기 - 없을 경우")
    @Test
    fun exists_by_user_id_and_place_id_not_found() {

        val result = reviewRepository.existsByUserIdAndPlaceId(
            UUID.randomUUID(),
            UUID.randomUUID(),
        )

        assertThat(result).isFalse
    }

    @DisplayName("유저 ID, 장소 ID로 리뷰 찾기 - 있을 경우")
    @Test
    fun exists_by_user_id_and_place_id_found() {

        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            attachedPhotos = AttachedPhotos(),
            isFirstReview = true,
        )
        reviewRepository.save(review)
        flushAndClear()

        val result = reviewRepository.existsByUserIdAndPlaceId(
            review.userId,
            review.placeId,
        )

        assertThat(result).isTrue
    }

    @DisplayName("장소 ID로 리뷰 조회 - 없을 경우")
    @Test
    fun exists_by_place_id_not_found() {

        val result = reviewRepository.existsByPlaceId(UUID.randomUUID())

        assertThat(result).isFalse
    }

    @DisplayName("장소 ID로 리뷰 조회 - 있을 경우")
    @Test
    fun exists_by_place_id_found() {

        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            attachedPhotos = AttachedPhotos(),
            isFirstReview = true,
        )
        reviewRepository.save(review)
        flushAndClear()

        val result = reviewRepository.existsByPlaceId(review.placeId)

        assertThat(result).isTrue
    }

    @DisplayName("리뷰와 사진을 함께 조회 - 리뷰가 없을 경우")
    @Test
    fun find_with_attached_photos_not_found() {

        val review = reviewRepository.findWithAttachedPhotosById(UUID.randomUUID())

        assertThat(review).isNull()
    }

    @DisplayName("리뷰와 사진을 함께 조회 - 리뷰가 있고 첨부사진이 없을 경우")
    @Test
    fun find_with_attached_photos_found_not_exist_attached_photos() {

        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            attachedPhotos = AttachedPhotos(),
            isFirstReview = true,
        )
        reviewRepository.save(review)
        flushAndClear()

        val foundReview = reviewRepository.findWithAttachedPhotosById(review.id)

        assertThat(foundReview).isNotNull
        assertThat(foundReview?.attachedPhotos).isNotNull
        assertThat(foundReview?.attachedPhotos?.attachedPhotos?.size)
            .isEqualTo(0)
    }

    @DisplayName("리뷰와 사진을 함께 조회 - 리뷰와 첨부사진이 있을 경우")
    @Test
    fun find_with_attached_photos() {

        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            content = "테스트 컨텐츠",
            attachedPhotos = AttachedPhotos(mutableSetOf(
                AttachedPhoto(UUID.randomUUID()),
                AttachedPhoto(UUID.randomUUID()),
            )),
            isFirstReview = true,
        )
        reviewRepository.save(review)
        flushAndClear()

        val foundReview = reviewRepository.findWithAttachedPhotosById(review.id)

        assertThat(foundReview).isNotNull
        assertThat(foundReview?.attachedPhotos).isNotNull
        assertThat(foundReview?.attachedPhotos?.attachedPhotos?.size)
            .isEqualTo(2)
    }
}
