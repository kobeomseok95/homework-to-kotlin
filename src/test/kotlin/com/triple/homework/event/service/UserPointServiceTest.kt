package com.triple.homework.event.service

import com.triple.homework.event.domain.User
import com.triple.homework.event.domain.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.util.*

internal class UserPointServiceTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val userPointService: UserPointService = UserPointService(
        userRepository
    )

    @DisplayName("리뷰 생성 - 성공 / 유저가 없을 경우 유저와 포인트 저장")
    @Test
    fun save_user_and_calculate_user_point() {
        val userId = UUID.randomUUID()
        val savePoint = 3
        every {
            userRepository.findByIdOrNull(userId)
        } returns null
        val user = User(
            id = userId,
            point = savePoint.toLong(),
        )
        every {
            userRepository.save(any())
        } returns user

        userPointService.saveUserIfNotFoundAndCalculateUserPoint(userId, savePoint)

        verify {
            userRepository.findByIdOrNull(userId)
            userRepository.save(any())
        }
    }

    @DisplayName("리뷰 생성 - 성공 / 유저가 있을 경우 유저를 찾아 포인트 반영")
    @Test
    fun find_user_and_calculate_user_point() {
        val userId = UUID.randomUUID()
        val savePoint = 3
        val beforeUserPoint = 10L
        val user = User(
            id = userId,
            point = beforeUserPoint,
        )
        every {
            userRepository.findByIdOrNull(userId)
        } returns user

        userPointService.saveUserIfNotFoundAndCalculateUserPoint(userId, savePoint)

        assertThat(user.point)
            .isEqualTo(beforeUserPoint + savePoint)
        verify {
            userRepository.findById(userId)
        }
    }
}
