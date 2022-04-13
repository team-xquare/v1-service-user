package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.xquare.v1userservice.user.User
import io.smallrye.mutiny.converters.uni.UniReactorConverters
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory
): UserRepository {
    override suspend fun save(user: User): User {
        reactiveQueryFactory.transactionWithFactory { session, _ ->
            session.persist(user)
                .await()
        }
        return user
    }

    override suspend fun findByIdOrNull(id: UUID): User? {
        return reactiveQueryFactory.withFactory { session, reactiveQueryFactory ->
            session.find(User::class.java, id)
                .convert().with(UniReactorConverters.toMono())
                .awaitSingleOrNull()
        }
    }
}