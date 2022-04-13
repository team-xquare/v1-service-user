package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserEntity
import com.xquare.v1userservice.user.mapper.UserDomainMapper
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Repository
import java.util.*

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
        val userEntity = reactiveQueryFactory.withFactory { session, reactiveQueryFactory ->
            session.find(UserEntity::class.java, id)
                .convert().with(UniReactorConverters.toMono())
                .awaitSingleOrNull()
        }

        return userEntity?.let { userDomainMapper.userEntityToDomain(it) }
    }
}