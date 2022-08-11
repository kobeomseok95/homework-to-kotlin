package com.triple.homework.event.domain

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class UserTest {

    @DisplayName("유저 포인트 변경 - 양수일 경우")
    @Test
    fun change_point_positive_number_point() {
        val user = User(
            id = UUID.randomUUID(),
            point = 1L,
        )
        val beforeChangedUserPoint = user.point
        val receivedPoint = 2

        user.changePoint(receivedPoint)

        assertThat(user.point)
            .isEqualTo(beforeChangedUserPoint + receivedPoint)
    }

    @DisplayName("유저 포인트 변경 - 음수일 경우 0으로 변경")
    @Test
    fun change_point_negative_number_point() {
        val user = User(
            id = UUID.randomUUID(),
            point = 1L,
        )
        val receivedPoint = -2


        user.changePoint(receivedPoint)

        assertThat(user.point)
            .isEqualTo(0)
    }
}
