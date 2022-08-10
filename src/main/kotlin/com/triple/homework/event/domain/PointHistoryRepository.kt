package com.triple.homework.event.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PointHistoryRepository: JpaRepository<PointHistory, Long> {
}