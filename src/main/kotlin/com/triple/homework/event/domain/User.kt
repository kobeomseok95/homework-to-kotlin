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
        // TODO: test 음수일 경우는 어떻게하지?
        this.point += point
    }
}