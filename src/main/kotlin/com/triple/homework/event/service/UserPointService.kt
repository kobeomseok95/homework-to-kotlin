package com.triple.homework.event.service

import com.triple.homework.common.exception.user.UserNotFoundException
import com.triple.homework.event.domain.User
import com.triple.homework.event.domain.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserPointService(
    private val userRepository: UserRepository,
) {
    fun saveUserIfNotFoundAndCalculateUserPoint(userId: UUID, savedPoint: Int) {
        val user: User? = userRepository.findByIdOrNull(userId)
        if (user == null) {
            userRepository.save(User(
                id = userId,
                point = savedPoint.toLong(),
            ))
        } else {
            user.changePoint(savedPoint)
        }
    }

    fun changeUserPoint(userId: UUID, savedPoint: Int) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()
        user.changePoint(savedPoint)
    }
}