package com.triple.homework.domain

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

): BaseTimeEntity()