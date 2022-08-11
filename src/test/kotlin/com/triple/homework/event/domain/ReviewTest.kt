package com.triple.homework.event.domain

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class ReviewTest {

    @DisplayName("리뷰에 사진이 있는지 여부 - 있을 경우")
    @Test
    fun review_have_photos_success() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(listOf(
                AttachedPhoto(UUID.randomUUID()),
                AttachedPhoto(UUID.randomUUID()),
            ))
        )

        assertThat(review.havePhotos)
            .isTrue
    }

    @DisplayName("리뷰에 사진이 있는지 여부 - 없을 경우")
    @Test
    fun review_have_not_photos_success() {
        val review = Review(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            placeId = UUID.randomUUID(),
            isFirstReview = false,
            attachedPhotos = AttachedPhotos(listOf())
        )

        assertThat(review.havePhotos)
            .isFalse
    }
}
