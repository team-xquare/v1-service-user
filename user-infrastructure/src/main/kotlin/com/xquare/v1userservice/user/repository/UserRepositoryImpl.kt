package com.xquare.v1userservice.user.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import com.xquare.v1userservice.user.User
import java.util.*

class UserRepositoryImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory
) : UserRepository {
    override suspend fun save(user: User): User {
        reactiveQueryFactory.withFactory { session, _ ->
            session.persist(user)
                .await()
        }
        return user
    }

    override suspend fun findByIdOrNull(id: UUID): User? {
        return reactiveQueryFactory.withFactory { _, factory ->
            factory.selectQuery<User> {
                select(entity(User::class))
                from(entity(User::class))
                where(col(User::id).equal(id))
            }
        }.singleResultOrNull()
    }
}