package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.ReactiveQueryFactory
import com.linecorp.kotlinjdsl.deleteQuery
import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.singleQueryOrNull
import com.xquare.v1userservice.configuration.cdc.OutboxEntity
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserEntity
import com.xquare.v1userservice.user.UserState
import com.xquare.v1userservice.user.mapper.UserDomainMapper
import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.json.JsonObject
import java.sql.Timestamp
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
    override suspend fun saveUserAndOutbox(user: User): User {
        val userEntityToSave = userDomainMapper.userDomainToEntity(user)
        val outboxEntityToSave = user.toOutboxEntity()
        reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.persistOutboxEntity(outboxEntityToSave)
            session.persistUserEntityConcurrently(userEntityToSave)
        }

        reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.deleteOutboxEntity(outboxEntityToSave.id)
        }

        return user
    }

    private fun User.toOutboxEntity() =
        OutboxEntity(
            id = UUID.randomUUID(),
            type = "UserCreated",
            aggregateType = "user",
            aggregateId = this.id,
            payload = buildOutboxPayloadJson(this),
            timestamp = Timestamp(System.currentTimeMillis())
        )

    private fun buildOutboxPayloadJson(user: User) =
        JsonObject().apply {
            put("user_id", user.id)
        }

    private suspend fun Session.persistOutboxEntity(outboxEntity: OutboxEntity) {
        this.persist(outboxEntity).awaitSuspending()
    }

    private suspend fun Session.persistUserEntityConcurrently(userEntity: UserEntity) = coroutineScope {
        launch {
            this@persistUserEntityConcurrently.persist(userEntity).awaitSuspending()
        }
    }

    private suspend fun ReactiveQueryFactory.deleteOutboxEntity(id: UUID) {
        this.deleteQuery<OutboxEntity> {
            where(col(OutboxEntity::id).equal(id))
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

    private suspend fun ReactiveQueryFactory.findByAccountIdAndUserState(accountId: String, state: UserState): UserEntity? {
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
}
