package com.triple.homework.event.domain

import javax.persistence.*

@Embeddable
class AttachedPhotos(

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "ATTACHED_PHOTO",
        joinColumns = [ JoinColumn(name = "REVIEW_ID") ],
    )
    @OrderColumn(name = "ATTACHED_PHOTO_INDEX")
    val attachedPhotos: MutableSet<AttachedPhoto> = mutableSetOf()

) {

    val havePhotos: Boolean
        get() = attachedPhotos.isNotEmpty()

    fun update(target: MutableSet<AttachedPhoto>) {
        attachedPhotos.clear()
        attachedPhotos.addAll(target)
    }
}
