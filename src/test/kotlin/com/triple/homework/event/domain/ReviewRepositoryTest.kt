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
        val userId = review.userId
        val placeId = review.placeId
        reviewRepository.save(review)
        flushAndClear()

        val result = reviewRepository.existsByUserIdAndPlaceId(
            userId,
            placeId,
        )

        assertThat(result).isTrue
    }
}
