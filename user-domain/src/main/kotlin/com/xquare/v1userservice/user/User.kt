package com.xquare.v1userservice.user

import com.xquare.v1userservice.annotations.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class User(
    val name: String,

    val entranceYear: Int,

    val birthDay: LocalDate,

    val grade: Int,

    val profileFileName: String?,

    val password: String,

    val accountId: String,

    val classNum: Int,

    val num: Int,

    val deviceToken: String? = null,

    val state: UserState = UserState.CREATE_PENDING,

    val id: UUID = UUID.randomUUID(),

    val role: UserRole
) {
    fun setUserStateToCreated(): User {
        return copy(state = UserState.CREATED)
    }

    fun setDeviceToken(deviceToken: String): User {
        return copy(deviceToken = deviceToken)
    }

    fun updateProfileFileName(profileFileName: String?): User {
        return this.copy(profileFileName = profileFileName)
    }
}
