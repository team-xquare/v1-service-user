package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.ReactiveQueryFactory
import com.linecorp.kotlinjdsl.deleteQuery
import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import com.linecorp.kotlinjdsl.singleQueryOrNull
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserEntity
import com.xquare.v1userservice.user.UserState
import com.xquare.v1userservice.user.mapper.UserDomainMapper
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.hibernate.reactive.mutiny.Mutiny.Session
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory,
    private val userDomainMapper: UserDomainMapper
) : UserRepositorySpi {
    override suspend fun saveUser(user: User): User {
        val userEntityToSave = userDomainMapper.userDomainToEntity(user)
        reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.persistUserEntityConcurrently(userEntityToSave)
        }

        return user
    }

    private suspend fun Session.persistUserEntityConcurrently(userEntity: UserEntity) = coroutineScope {
        launch {
            this@persistUserEntityConcurrently.persist(userEntity).awaitSuspending()
        }
    }

    override suspend fun findByIdAndStateWithCreatePending(id: UUID): User? {
        val userEntity = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.findByIdAndUserState(id, UserState.CREATE_PENDING)
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }

    override suspend fun findByIdAndStateWithCreated(userId: UUID): User? {
        val userEntity = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.findByIdAndUserState(userId, UserState.CREATED)
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }

    private suspend fun ReactiveQueryFactory.findByIdAndUserState(id: UUID, state: UserState): UserEntity? {
        return this.singleQueryOrNull<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(
                col(UserEntity::id).equal(id)
                    .and(col(UserEntity::state).equal(state))
            )
        }
    }

    override suspend fun findByAccountIdAndStateWithCreated(accountId: String): User? {
        val userEntity = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.findByAccountIdAndUserState(accountId, UserState.CREATED)
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }

    private suspend fun ReactiveQueryFactory.findByAccountIdAndUserState(
        accountId: String,
        state: UserState
    ): UserEntity? {
        return this.singleQueryOrNull<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(
                col(UserEntity::accountId).equal(accountId)
                    .and(col(UserEntity::state).equal(state))
            )
        }
    }

    override suspend fun applyChanges(user: User): User {
        val userEntityToUpdate = userDomainMapper.userDomainToEntity(user)
        val updatedEntity = reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.mergeUserEntity(userEntityToUpdate)
        }
        return userDomainMapper.userEntityToDomain(updatedEntity)
    }

    private suspend fun Session.mergeUserEntity(userEntity: UserEntity) =
        this.merge(userEntity).awaitSuspending()

    override suspend fun deleteByIdAndStateWithCreatePending(id: UUID) {
        reactiveQueryFactory.transactionWithFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.deleteWithUserIdAndState(id, UserState.CREATE_PENDING)
        }
    }

    private suspend fun ReactiveQueryFactory.deleteWithUserIdAndState(id: UUID, userState: UserState) {
        this.deleteQuery<UserEntity> {
            where(
                col(UserEntity::id).equal(id)
                    .and(col(UserEntity::state).equal(userState))
            )
        }.executeUpdate()
    }

    override suspend fun findAllByIdIn(idList: List<UUID>): List<User> {
        return reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.findAllByIdsIn(idList)
        }.map { userDomainMapper.userEntityToDomain(it) }
    }

    private suspend fun ReactiveQueryFactory.findAllByIdsIn(ids: List<UUID>): List<UserEntity> {
        return this.selectQuery<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(col(UserEntity::id).`in`(ids))
        }.resultList()
    }

    override suspend fun findAllByGradeAndClass(grade: Int, classNum: Int): List<User> {
        return reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.findAllByGradeAndClass(grade, classNum)
        }.map { userDomainMapper.userEntityToDomain(it) }
    }

    private suspend fun ReactiveQueryFactory.findAllByGradeAndClass(grade: Int, classNum: Int): List<UserEntity> {
        return if (classNum == 0) {
            this.selectQuery<UserEntity> {
                select(entity(UserEntity::class))
                from(entity(UserEntity::class))
                where(col(UserEntity::grade).equal(grade))
            }.resultList()
        } else this.selectQuery<UserEntity> {
            select(entity(UserEntity::class))
            from(entity(UserEntity::class))
            where(
                col(UserEntity::grade).equal(grade)
                    .and(col(UserEntity::classNum).equal(classNum))
            )
        }.resultList()
    }
}
