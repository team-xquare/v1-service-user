package com.xquare.v1userservice.domain

import com.xquare.v1userservice.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Year
import java.util.*

internal class UserEntityEntityTest {

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
            entranceYear = Year.now(),
            grade = 1,
            num = 2,
            profileFileName = "sdaf",
            id = UUID.randomUUID()
        )

}