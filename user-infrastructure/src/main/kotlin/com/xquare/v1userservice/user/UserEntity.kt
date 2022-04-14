package com.xquare.v1userservice.user

import java.time.LocalDate
import java.time.Year
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_user")
class UserEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @field:Size(max = 5)
    val name: String,

    val entranceYear: Year,

    val birthDay: LocalDate,

    val grade: Int,

    @field:Size(max = 255)
    val profileFileName: String,

    val password: String,

    val accountId: String,

    classNum: Int,

    num: Int,

    deviceToken: String,
) {

    var classNum = classNum
        protected set

    var num = num
        protected set

    var deviceToken = deviceToken
        protected set
}
