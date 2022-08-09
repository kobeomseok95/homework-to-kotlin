package com.triple.homework.domain

import javax.persistence.Embeddable
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Embeddable
class AttachedPhotos(
    
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "review"
    )
    val attachedPhotos: List<AttachedPhoto> = listOf()

)
