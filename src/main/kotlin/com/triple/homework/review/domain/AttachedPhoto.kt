package com.triple.homework.review.domain

import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class AttachedPhoto(

    @Column
    val attachedPhotoId: UUID,
)