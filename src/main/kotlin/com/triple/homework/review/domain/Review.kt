package com.triple.homework.review.domain

import com.triple.homework.common.BaseTimeEntity
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

   val userId: UUID,

   val placeId: UUID,

   var content: String?,

   @Embedded
   var attachedPhotos: AttachedPhotos?,

): BaseTimeEntity()