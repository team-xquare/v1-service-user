package com.xquare.v1userservice.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun setUserStateToCreated() {
        val user = UserUtils.buildUserWithCreatePendingState()
        val modifiedUser = user.setUserStateToCreated()
        assertThat(modifiedUser.state).isEqualTo(UserState.CREATED)
    }
}
