package com.triple.homework.event.domain

import javax.persistence.*

@Embeddable
class AttachedPhotos(

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "ATTACHED_PHOTO",
        joinColumns = [ JoinColumn(name = "REVIEW_ID") ],
    )
    val attachedPhotos: List<AttachedPhoto>

) {

    val havePhotos: Boolean
        get() = attachedPhotos.isNotEmpty()
}
