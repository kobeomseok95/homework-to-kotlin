package com.triple.homework.event.domain

import com.triple.homework.common.BaseTimeEntity
import java.util.*
import javax.persistence.*

@Entity
class PointHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_HISTORY_ID")
    val id: Long? = null,

    val userId: UUID,

    @Enumerated(EnumType.STRING)
    val pointType: PointType,

): BaseTimeEntity()