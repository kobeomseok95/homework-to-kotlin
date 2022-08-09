package com.triple.homework.domain

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Review(

   @Id
   @Column(name = "REVIEW_ID")
   val id: Long,

   val userId: Long,

   val placeId: Long,

   val content: String?,

   @Embedded
   val attachedPhotos: AttachedPhotos

): BaseTimeEntity()