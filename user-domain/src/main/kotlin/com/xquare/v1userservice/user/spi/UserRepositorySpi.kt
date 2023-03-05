package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Spi
import com.xquare.v1userservice.user.User
import java.util.UUID

@Spi
interface UserRepositorySpi {
    suspend fun saveUser(user: User): User
    suspend fun findByIdAndStateWithCreatePending(id: UUID): User?
    suspend fun findByIdAndStateWithCreated(userId: UUID): User?
    suspend fun findByAccountIdAndStateWithCreated(accountId: String): User?
    suspend fun applyChanges(user: User): User
    suspend fun deleteByIdAndStateWithCreatePending(id: UUID)
    suspend fun findAllByIdIn(idList: List<UUID>): List<User>
    suspend fun findAllByGradeAndClass(grade: Int, classNum: Int?): List<User>
    suspend fun findAllUser(): List<User>
    suspend fun findAllTeacher(): List<User>
}
