package com.xquare.v1userservice.user.service

import com.xquare.v1userservice.stubs.InMemoryUserRepository
import com.xquare.v1userservice.user.UserUtils
import com.xquare.v1userservice.user.api.impl.UpdateUserCreatedStateStepImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UpdateUserCreatedStateStepImplTest {

    private val user = UserUtils.buildUserWithCreatePendingState()
    private var userRepositorySpi = InMemoryUserRepository(hashMapOf(user.id to user))
    private var updateUserCreatedStateStep = UpdateUserCreatedStateStepImpl(userRepositorySpi)

    @BeforeEach
    fun setup() {
        userRepositorySpi = InMemoryUserRepository(hashMapOf(user.id to user))
        updateUserCreatedStateStep = UpdateUserCreatedStateStepImpl(userRepositorySpi)
    }

    @Test
    fun processStepSuccessTest() = runTest {
        updateUserCreatedStateStep.processStep(user.id)
        assertThat(userRepositorySpi.findByIdAndStateWithCreatePending(user.id)).isNull()
    }
}
