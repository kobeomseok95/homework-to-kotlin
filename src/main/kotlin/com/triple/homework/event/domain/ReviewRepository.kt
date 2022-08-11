package com.triple.homework.event.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReviewRepository: JpaRepository<Review, UUID> {

    fun existsByUserIdAndPlaceId(userId: UUID, placeId: UUID): Boolean

    fun existsByPlaceId(placeId: UUID): Boolean

    @Query("""
        select r 
        from Review r
        left join fetch r.attachedPhotos.attachedPhotos
        where r.id = :reviewId
    """)
    fun findWithAttachedPhotosById(@Param("reviewId") reviewId: UUID): Review?
}