package com.xquare.v1userservice.user

import java.time.LocalDate
import java.time.Year
import java.util.UUID

object UserUtils {
    fun buildUserWithCreatePendingState() = User(
        name = "name",
        password = "testPassword",
        accountId = "accountId",
        birthDay = LocalDate.now(),
        classNum = 2,
        deviceToken = "sdaf",
        entranceYear = Year.now().value,
        grade = 1,
        num = 2,
        profileFileName = "sdaf",
        id = UUID.randomUUID(),
        role = UserRole.STU
    )

    fun buildCreatedUser() = User(
        name = "name",
        password = "testPassword",
        accountId = "accountId",
        birthDay = LocalDate.now(),
        classNum = 2,
        deviceToken = "sdaf",
        entranceYear = Year.now().value,
        grade = 1,
        num = 2,
        profileFileName = "sdaf",
        id = UUID.randomUUID(),
        role = UserRole.STU
    ).setUserStateToCreated()
}
