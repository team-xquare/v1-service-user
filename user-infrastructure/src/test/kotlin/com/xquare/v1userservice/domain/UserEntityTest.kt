package com.xquare.v1userservice.domain

import com.xquare.v1userservice.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Year

internal class UserEntityTest {

    private var user: User? = null

    @BeforeEach
    fun createEntity() {
        user = buildCompletedUser()
    }

    @Test
    fun validateUserIdNotNull() {
        assertThat(user?.id).isNotNull
    }

    private fun buildCompletedUser() =
        User(
            name = "name",
            password = "testPassword",
            accountId = "accountId",
            birthDay = LocalDate.now(),
            classNum = 2,
            deviceToken = "sdaf",
            entranceYear = Year.now(),
            grade = 1,
            num = 2,
            profileFileName = "sdaf"
        )

}