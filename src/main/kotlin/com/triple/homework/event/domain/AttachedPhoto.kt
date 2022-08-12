package com.triple.homework.event.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class AttachedPhoto(

    @Column(name = "ATTACHED_PHOTO_ID", columnDefinition = "BINARY(16)")
    val attachedPhotoId: UUID,
)