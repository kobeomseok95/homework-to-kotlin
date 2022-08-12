package com.triple.homework.event.controller.request

import com.triple.homework.common.validation.EnumValid
import java.util.*

data class EventRequest (

    @field:EnumValid(
        message = "존재하지 않는 이벤트 타입입니다.",
        enumClass = EventType::class,
    )
    val type: String?,

    @field:EnumValid(
        message = "존재하지 않는 이벤트 액션입니다.",
        enumClass = ActionType::class,
    )
    val action: String?,

    val reviewId: UUID?,

    val content: String?,

    val attachedPhotoIds: List<UUID>? = mutableListOf(),

    val userId: UUID?,

    val placeId: UUID?,
)