package com.triple.homework.event.domain

import com.triple.homework.common.BaseTimeEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "USERS")
class User(

    @Id
    @Column(name = "USER_ID")
    val id: UUID,

    var point: Long,

): BaseTimeEntity() {

    fun changePoint(point: Int) {
        this.point += point
        if (this.point < 0) {
            this.point = 0
        }
    }
}