package com.triple.homework.event.service.dto.request

import com.triple.homework.event.domain.AttachedPhoto
import com.triple.homework.event.domain.AttachedPhotos
import com.triple.homework.event.domain.Review
import java.util.*

data class ReviewRequestDto(

    val reviewId: UUID,
    val content: String? = null,
    val attachedPhotoIds: List<UUID>,
    val userId: UUID,
    val placeId: UUID,
) {

    fun toReview() = Review(
        id = reviewId,
        content = content,
        attachedPhotos =
            if (attachedPhotoIds.isNotEmpty()) toAttachedPhotos()
            else null,
        userId = userId,
        placeId = placeId,
    )

    private fun toAttachedPhotos() = AttachedPhotos(
        attachedPhotoIds.map {
            AttachedPhoto(it)
        }.toList(),
    )
}
