package com.triple.homework.event.service

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
        userRepository.findByIdOrNull(userId)
            ?.let {
                it.changePoint(savedPoint)
            }
            ?: userRepository.save(User(
                id = userId,
                point = savedPoint.toLong(),
            ))
    }
}