package com.triple.homework.review.domain

import com.triple.homework.common.BaseTimeEntity
import java.util.*
import javax.persistence.*

@Entity
class AttachedPhoto(

    @Id
    @Column(name = "ATTACHED_PHOTO_ID")
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    val review: Review,

    ): BaseTimeEntity()