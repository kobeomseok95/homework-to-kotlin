package com.triple.homework.event.domain

import com.triple.homework.common.BaseTimeEntity
import com.triple.homework.common.exception.review.ReviewerNotEqualsException
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Review(

   @Id
   @Column(name = "REVIEW_ID")
   val id: UUID,

   @Column(nullable = false)
   val userId: UUID,

   @Column(nullable = false)
   var placeId: UUID,

   var content: String? = null,

   @Embedded
   val attachedPhotos: AttachedPhotos,

   @Column(nullable = false)
   var isFirstReview: Boolean,

): BaseTimeEntity() {

    val havePhotos: Boolean
        get() = attachedPhotos.havePhotos

    val hasContent: Boolean
        get() = !content.isNullOrBlank()

    fun update(userId: UUID,
               placeId: UUID,
               content: String?,
               attachedPhotos: MutableSet<AttachedPhoto>,
               isFirstReview: Boolean,
    ) {
        checkEquals(userId)
        this.placeId = placeId
        this.content = content
        this.attachedPhotos.update(attachedPhotos)
        this.isFirstReview = isFirstReview
    }

    private fun checkEquals(userId: UUID) {
        if (this.userId != userId)
            throw ReviewerNotEqualsException()
    }
}