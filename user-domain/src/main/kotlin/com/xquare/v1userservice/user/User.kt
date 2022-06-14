package com.xquare.v1userservice.user

import com.xquare.v1userservice.annotations.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
class User(
    val name: String,

    val entranceYear: Int,

    val birthDay: LocalDate,

    val grade: Int,

    val profileFileName: String,

    val password: String,

    val accountId: String,

    classNum: Int,

    num: Int,

    deviceToken: String? = null,

    val id: UUID = UUID.randomUUID(),

    val role: UserRole
) {

    var state = UserState.CREATE_PENDING
        private set

    var classNum = classNum
        private set

    var num = num
        private set

    var deviceToken = deviceToken
        private set

    fun setUserStateToCreated() {
        this.state = UserState.CREATED
    }
}
