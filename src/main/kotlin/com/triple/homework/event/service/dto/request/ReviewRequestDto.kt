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

    fun toReview(isFirstReview: Boolean) = Review(
        id = reviewId,
        content = content,
        attachedPhotos = toAttachedPhotos(),
        userId = userId,
        placeId = placeId,
        isFirstReview = isFirstReview,
    )

    fun toAttachedPhotos() = if (attachedPhotoIds.isNotEmpty()) {
        AttachedPhotos(
            attachedPhotoIds.map {
                AttachedPhoto(it)
            }.toList()
        )
    } else {
        null
    }
}
