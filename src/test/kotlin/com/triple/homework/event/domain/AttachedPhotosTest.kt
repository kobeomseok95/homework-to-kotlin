package com.triple.homework.event.domain

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class AttachedPhotosTest {

    @DisplayName("첨부사진 수정 - 1장에서 2장으로 수정")
    @Test
    fun update_attached_photo_one_photo_to_be_two_photos() {
        val attachedPhotos = AttachedPhotos(mutableSetOf(
            AttachedPhoto(UUID.randomUUID()),
        ))
        val target = mutableSetOf(
            AttachedPhoto(UUID.randomUUID()),
            AttachedPhoto(UUID.randomUUID()),
        )

        attachedPhotos.update(target)

        assertThat(attachedPhotos.attachedPhotos)
            .hasSize(target.size)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(target)
    }

    @DisplayName("첨부사진 수정 - 0장에서 2장으로 수정")
    @Test
    fun update_attached_photo_zero_photo_to_be_two_photos() {
        val attachedPhotos = AttachedPhotos()
        val target = mutableSetOf(
            AttachedPhoto(UUID.randomUUID()),
            AttachedPhoto(UUID.randomUUID()),
        )

        attachedPhotos.update(target)

        assertThat(attachedPhotos.attachedPhotos)
            .hasSize(target.size)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(target)
    }



    @DisplayName("첨부사진 수정 - 2장에서 0장으로 수정")
    @Test
    fun update_attached_photo_two_photos_to_be_zero_photo() {
        val attachedPhotos = AttachedPhotos(mutableSetOf(
            AttachedPhoto(UUID.randomUUID()),
            AttachedPhoto(UUID.randomUUID()),
        ))
        val target = mutableSetOf<AttachedPhoto>()

        attachedPhotos.update(target)

        assertThat(attachedPhotos.attachedPhotos)
            .hasSize(target.size)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(target)
    }
}
