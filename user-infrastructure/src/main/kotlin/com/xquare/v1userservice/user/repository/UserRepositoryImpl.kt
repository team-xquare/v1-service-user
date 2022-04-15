package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.singleQueryOrNull
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserEntity
import com.xquare.v1userservice.user.mapper.UserDomainMapper
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory,
    private val userDomainMapper: UserDomainMapper
) : UserRepositorySpi {
    override suspend fun save(user: User): User {
        val userEntityToSave = userDomainMapper.userDomainToEntity(user)
        reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.persist(userEntityToSave)
                .awaitSuspending()
        }
        return user
    }

    override suspend fun findByIdOrNull(id: UUID): User? {
        val userEntity = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.singleQueryOrNull<UserEntity> {
                select(entity(UserEntity::class))
                from(entity(UserEntity::class))
                where(col(UserEntity::id).equal(id))
            }
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }
}
