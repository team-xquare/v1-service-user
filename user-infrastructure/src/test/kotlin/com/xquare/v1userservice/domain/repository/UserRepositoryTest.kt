package com.xquare.v1userservice.domain.repository

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.test.context.TestConstructor
import java.time.LocalDate
import java.time.Year

@AutoConfigureTestEntityManager
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class UserRepositoryTest(
    private val userRepository: UserRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createEntity() = runTest {
        val user = buildUnCompletedUser()
        val test = userRepository.save(user)
    }

    private fun buildUnCompletedUser(): User {
        return User(
            name = "test",
            password = "testPassword",
            accountId = "accountId",
            birthDay = LocalDate.now(),
            classNum = 2,
            deviceToken = "sdaf",
            entranceYear = Year.of(2022),
            grade = 1,
            num = 2,
            profileFileName = "sdaf"
        )
    }

}