package com.triple.homework.event.domain

import com.triple.homework.common.BaseTimeEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Review(

   @Id
   @Column(name = "REVIEW_ID")
   val id: UUID,

   val userId: UUID,

   val placeId: UUID,

   var content: String? = null,

   @Embedded
   var attachedPhotos: AttachedPhotos? = null,

): BaseTimeEntity()