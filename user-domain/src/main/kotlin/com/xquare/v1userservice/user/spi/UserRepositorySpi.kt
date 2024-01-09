package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Spi
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserRole
import java.util.UUID

@Spi
interface UserRepositorySpi {

    //TODO 2024 : 1/31 반환타입 추가하기
    //suspend fun saveAll(user: List<User>): List<>

    suspend fun saveUser(user: User): User
    suspend fun findByIdAndStateWithCreatePending(id: UUID): User?
    suspend fun findByIdAndStateWithCreated(userId: UUID): User?
    suspend fun findByAccountIdAndStateWithCreated(accountId: String): User?
    suspend fun applyChanges(user: User): User
    suspend fun deleteByIdAndStateWithCreatePending(id: UUID)
    suspend fun findAllByIdIn(idList: List<UUID>): List<User>
    suspend fun findAllByGradeAndClass(grade: Int, classNum: Int?): List<User>
    suspend fun findAllStudent(): List<User>
    suspend fun findAllTeacher(): List<User>
    suspend fun findStudentByName(name: String): List<User>
    suspend fun findAllByRole(userRole: UserRole?): List<User>
    suspend fun findAllByUserIdNotIn(userIdList: List<UUID>?): List<UUID>
}
