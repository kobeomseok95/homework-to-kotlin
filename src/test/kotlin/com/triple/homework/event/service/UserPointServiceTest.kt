package com.triple.homework.event.service

import com.triple.homework.common.exception.user.UserNotFoundException
import com.triple.homework.event.domain.User
import com.triple.homework.event.domain.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @DisplayName("유저의 포인트 변경 - 실패 / 유저가 존재하지 않을 경우")
    @Test
    fun change_user_point_fail_not_found_user() {

        val userId = UUID.randomUUID()
        every {
            userRepository.findByIdOrNull(userId)
        } returns null

        assertThrows<UserNotFoundException> {
            userPointService.changeUserPoint(userId, 1)
        }
        verify {
            userRepository.findByIdOrNull(userId)
        }
    }

    @DisplayName("유저의 포인트 변경 - 성공")
    @Test
    fun change_user_point_success() {

        val user = User(
            id = UUID.randomUUID(),
            point = 10L,
        )
        val receivedPoint = 3
        val beforeChangeUserPoint = user.point
        every {
            userRepository.findByIdOrNull(user.id)
        } returns user

        userPointService.changeUserPoint(user.id, receivedPoint)

        assertThat(user.point).isEqualTo(beforeChangeUserPoint + receivedPoint)
        verify {
            userRepository.findByIdOrNull(user.id)
        }
    }

    @DisplayName("유저가 현재 보유한 포인트 내역 조회 - 실패 / 존재하지 않는 유저인 경우")
    @Test
    fun get_user_point_fail_not_found_user() {

        val user = User(
            id = UUID.randomUUID(),
            point = 3L,
        )
        every {
            userRepository.findByIdOrNull(user.id)
        } returns null

        assertThrows<UserNotFoundException> {
            userPointService.getUserPoint(user.id)
        }
        verify {
            userRepository.findByIdOrNull(user.id)
        }
    }

    @DisplayName("유저가 현재 보유한 포인트 내역 조회 - 성공")
    @Test
    fun get_user_point_success() {

        val user = User(
            id = UUID.randomUUID(),
            point = 3L,
        )
        every {
            userRepository.findByIdOrNull(user.id)
        } returns user

        val userPointResponseDto = userPointService.getUserPoint(user.id)

        assertThat(userPointResponseDto.point)
            .isEqualTo(user.point)
        assertThat(userPointResponseDto.userId)
            .isEqualTo(user.id)
    }
}
