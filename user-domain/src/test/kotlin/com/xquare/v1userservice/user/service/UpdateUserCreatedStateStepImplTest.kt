package com.xquare.v1userservice.user.service

import com.xquare.v1userservice.stubs.InMemoryUserRepository
import com.xquare.v1userservice.user.UserUtils
import com.xquare.v1userservice.user.saveuser.service.UpdateUserCreatedStateStepImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UpdateUserCreatedStateStepImplTest {

    private val user = UserUtils.buildUserWithCreatePendingState()
    private val userRepositorySpi = InMemoryUserRepository(hashMapOf(user.id to user))
    private val updateUserCreatedStateStep = UpdateUserCreatedStateStepImpl(userRepositorySpi)

    @Test
    fun processStepSuccessTest() = runTest {
        updateUserCreatedStateStep.processStep(user.id)
        assertThat(userRepositorySpi.findByIdAndStateWithCreatePending(user.id)).isNull()
    }
}
