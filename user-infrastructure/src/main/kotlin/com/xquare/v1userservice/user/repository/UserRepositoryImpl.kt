package com.xquare.v1userservice.user.repository

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
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory,
    private val userDomainMapper: UserDomainMapper
) : UserRepositorySpi {
    override suspend fun saveUserAndOutbox(user: User): User = coroutineScope {
        val userEntityToSave = userDomainMapper.userDomainToEntity(user)
        val outboxEntityToSave = user.toOutboxEntity()
        reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.persist(outboxEntityToSave)
                .awaitSuspending()

            // save user asynchronously
            session.persist(userEntityToSave)
                .awaitSuspending()
        }

        reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.deleteQuery<OutboxEntity> {
                where(col(OutboxEntity::id).equal(outboxEntityToSave.id))
            }
        }

        user
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
            put("id", user.id)
        }

    override suspend fun findByIdAndStateOrNull(id: UUID, state: UserState): User? {
        val userEntity = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.singleQueryOrNull<UserEntity> {
                select(entity(UserEntity::class))
                from(entity(UserEntity::class))
                where(
                    col(UserEntity::id).equal(id)
                        .and(col(UserEntity::state).equal(state))
                )
            }
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }

    override suspend fun applyChanges(user: User): User {
        val userEntityToUpdate = userDomainMapper.userDomainToEntity(user)
        val updatedEntity = reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.merge(userEntityToUpdate)
                .awaitSuspending()
        }
        return userDomainMapper.userEntityToDomain(updatedEntity)
    }

    override suspend fun deleteByIdAndState(id: UUID, userState: UserState): User? {
        return reactiveQueryFactory.transactionWithFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.deleteQuery(User::class) {
                where(
                    col(User::id).equal(id)
                        .and(col(User::state).equal(userState))
                )
            }
        }.singleResultOrNull()
    }
}
