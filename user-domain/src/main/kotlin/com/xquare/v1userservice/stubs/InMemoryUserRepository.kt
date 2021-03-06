package com.xquare.v1userservice.stubs

import com.xquare.v1userservice.annotations.Stub
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserState
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import java.util.UUID

@Stub
class InMemoryUserRepository(
    private val userMap: HashMap<UUID, User> = hashMapOf()
) : UserRepositorySpi {

    override suspend fun saveUser(user: User): User {
        userMap[user.id] = user
        return user
    }

    override suspend fun findByIdAndStateWithCreatePending(id: UUID): User? {
        val user = userMap[id]
        return if (user?.state == UserState.CREATE_PENDING) user else null
    }

    override suspend fun findByIdAndStateWithCreated(userId: UUID): User? {
        return if (userMap[userId]?.state == UserState.CREATED) {
            userMap[userId]
        } else {
            null
        }
    }

    override suspend fun findByAccountIdAndStateWithCreated(accountId: String): User? {
        return userMap.values.firstOrNull { it.accountId == accountId && it.state == UserState.CREATED }
    }

    override suspend fun applyChanges(user: User): User {
        userMap[user.id] = user
        return user
    }

    override suspend fun deleteByIdAndStateWithCreatePending(id: UUID) {
        if (userMap[id]?.state == UserState.CREATE_PENDING) {
            userMap.remove(id)
        }
    }

    override suspend fun findAllByIdIn(idList: List<UUID>): List<User> {
        return userMap.values.filter { idList.contains(it.id) }
    }
}
