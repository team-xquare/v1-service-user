package com.xquare.v1userservice.user.service

import com.xquare.v1userservice.stubs.InMemoryUserRepository
import com.xquare.v1userservice.user.UserUtils
import com.xquare.v1userservice.user.saveuser.service.CreateUserInPendingStateImpl
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class CreateUserInPendingStateImplTest {

    private val userRepositorySpi = InMemoryUserRepository()
    private val createUserInPendingState = CreateUserInPendingStateImpl(userRepositorySpi)

    @Test
    fun processStepSuccessTest() = runTest {
        val user = UserUtils.buildUserWithCreatePendingState()
        createUserInPendingState.processStep(user)
        assertTrue(isUserExists(user.id))
    }

    @Test
    fun revertStepSuccessTest() = runTest {
        val user = UserUtils.buildUserWithCreatePendingState()
        createUserInPendingState.processStep(user)
        createUserInPendingState.revertStep(user.id)
        assertFalse(isUserExists(user.id))
    }

    private suspend fun isUserExists(userId: UUID) =
        userRepositorySpi.findByIdAndStateWithCreatePending(userId) != null
}
