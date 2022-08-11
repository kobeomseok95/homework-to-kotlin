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

    val hasContent: Boolean
        get() = !content.isNullOrBlank()

    val havePhotos: Boolean
        get() = attachedPhotoIds.isNotEmpty()

    fun toReview(isFirstReview: Boolean) = Review(
        id = reviewId,
        content = content,
        attachedPhotos = toAttachedPhotos(),
        userId = userId,
        placeId = placeId,
        isFirstReview = isFirstReview,
    )

    private fun toAttachedPhotos() = if (havePhotos)
        AttachedPhotos(attachedPhotoIds.map {
            AttachedPhoto(it)
        }.toMutableSet())
    else AttachedPhotos()

    fun toAttachedPhotoSet() = if (havePhotos)
        attachedPhotoIds.map {
            AttachedPhoto(it)
        }.toMutableSet()
    else mutableSetOf()
}
