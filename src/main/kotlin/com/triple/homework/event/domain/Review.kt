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
   var attachedPhotos: AttachedPhotos? = null,

): BaseTimeEntity() {

    fun update(userId: UUID,
               placeId: UUID,
               content: String?,
               attachedPhotos: AttachedPhotos?
    ) {
        checkEquals(userId)
        this.placeId = placeId
        this.content = content
        this.attachedPhotos = attachedPhotos
    }

    private fun checkEquals(userId: UUID) {
        if (this.userId != userId)
            throw ReviewerNotEqualsException()
    }
}