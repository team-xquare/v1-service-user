package com.xquare.v1userservice.user.service

import com.xquare.v1userservice.stubs.InMemoryUserRepository
import com.xquare.v1userservice.user.UserState
import com.xquare.v1userservice.user.UserUtils
import com.xquare.v1userservice.user.saveuser.service.CreateUserInPendingStateImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class CreateUserInPendingStateImplTest {

    private val userRepositorySpi = InMemoryUserRepository()
    private val createUserInPendingState = CreateUserInPendingStateImpl(userRepositorySpi)

    @Test
    fun processStepSuccessTest() = runTest {
        val user = UserUtils.buildUserWithCreatePendingState()
        createUserInPendingState.processStep(user)
        assertThat(userRepositorySpi.findByIdAndStateOrNull(user.id, UserState.CREATE_PENDING)).isNotNull
    }

    @Test
    fun revertStepSuccessTest() = runTest {
        val user = UserUtils.buildUserWithCreatePendingState()
        createUserInPendingState.processStep(user)
        createUserInPendingState.revertStep(user.id)
        assertThat(userRepositorySpi.findByIdAndStateOrNull(user.id, UserState.CREATE_PENDING)).isNull()
    }
}
