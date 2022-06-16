package com.xquare.v1userservice.domain

import com.xquare.v1userservice.user.UserEntity
import com.xquare.v1userservice.user.UserRole
import com.xquare.v1userservice.user.UserState
import java.time.LocalDate
import java.time.Year
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserEntityTest {

    private var userEntity: UserEntity? = null

    @BeforeEach
    fun createEntity() {
        userEntity = buildCompletedUser()
    }

    @Test
    fun validateUserIdNotNull() {
        assertThat(userEntity?.id).isNotNull
    }

    private fun buildCompletedUser() =
        UserEntity(
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
            state = UserState.CREATE_PENDING,
            role = UserRole.STU
        )
}
