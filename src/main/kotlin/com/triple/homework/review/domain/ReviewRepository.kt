package com.triple.homework.review.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReviewRepository: JpaRepository<Review, UUID> {

    fun existsByUserIdAndPlaceId(userId: UUID, placeId: UUID): Boolean

    fun existsByReviewId(reviewId: UUID): Boolean
}