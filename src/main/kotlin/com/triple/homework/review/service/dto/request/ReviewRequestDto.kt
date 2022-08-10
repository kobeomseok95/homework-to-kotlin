package com.triple.homework.review.service.dto.request

import com.triple.homework.review.domain.AttachedPhoto
import com.triple.homework.review.domain.AttachedPhotos
import com.triple.homework.review.domain.Review
import java.util.*

data class ReviewRequestDto(

    val reviewId: UUID,
    val content: String? = null,
    val attachedPhotoIds: List<String>,
    val userId: UUID,
    val placeId: UUID,
) {

    fun toReview() =
        Review(
            id = reviewId,
            content = content,
            attachedPhotos =
                if (attachedPhotoIds.isNotEmpty()) toAttachedPhotos()
                else null,
            userId = userId,
            placeId = placeId,
        )

    private fun toAttachedPhotos() =
        AttachedPhotos(
            attachedPhotoIds.map {
                AttachedPhoto(UUID.fromString(it))
            }.toList()
        )
}
