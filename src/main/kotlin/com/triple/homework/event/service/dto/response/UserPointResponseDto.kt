package com.triple.homework.event.service.dto.response

import com.triple.homework.event.domain.User
import java.util.*

data class UserPointResponseDto private constructor(
    val userId: UUID,
    val point: Long,
) {
    constructor(user: User): this(
        userId = user.id,
        point = user.point,
    )
}