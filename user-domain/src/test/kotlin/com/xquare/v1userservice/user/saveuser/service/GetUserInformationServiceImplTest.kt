package com.xquare.v1userservice.user.saveuser.service

import com.xquare.v1userservice.stubs.InMemoryUserRepository
import com.xquare.v1userservice.user.EqualsTestUtils
import com.xquare.v1userservice.user.UserUtils
import com.xquare.v1userservice.user.exceptions.UserNotFoundException
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
internal class GetUserInformationServiceImplTest {

    private var userRepositorySpi: UserRepositorySpi = InMemoryUserRepository()
    private var getUserInformationService: GetUserInformationServiceImpl = GetUserInformationServiceImpl(userRepositorySpi)

    @BeforeEach
    fun setup() {
        userRepositorySpi = InMemoryUserRepository()
        getUserInformationService = GetUserInformationServiceImpl(userRepositorySpi)
    }

    @Test
    fun getUserById() = runTest {
        val user = saveUser()
        val userInformation = getUserInformationService.getUserById(user.id)

        EqualsTestUtils.isEqualTo(user, userInformation)
    }

    @Test
    fun getUserByIdNotFound() = runTest {
        assertThrows<UserNotFoundException> {
            getUserInformationService.getUserById(UUID.randomUUID())
        }
    }

    @Test
    fun getUserByAccountId() = runTest {
        val user = saveUser()
        val userInformation = getUserInformationService.getUserByAccountId(user.accountId)

        EqualsTestUtils.isEqualTo(user, userInformation)
    }

    @Test
    fun getUserByAccountIdNotFound() = runTest {
        assertThrows<UserNotFoundException> {
            getUserInformationService.getUserByAccountId("testId")
        }
    }

    private suspend fun saveUser() =
        userRepositorySpi.saveUser(UserUtils.buildCreatedUser())
}
