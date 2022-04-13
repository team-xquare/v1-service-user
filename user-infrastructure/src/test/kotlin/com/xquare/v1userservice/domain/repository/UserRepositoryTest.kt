package com.xquare.v1userservice.domain.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.singleQuery
import com.xquare.v1userservice.EmbeddedMySQLConfiguration
import com.xquare.v1userservice.EqualsTestUtil
import com.xquare.v1userservice.configuration.datasource.QueryBuilderConfig
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.repository.UserRepository
import com.xquare.v1userservice.user.repository.UserRepositoryImpl
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate
import java.time.Year
import java.util.*

@ExperimentalCoroutinesApi
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = [EmbeddedMySQLConfiguration::class, QueryBuilderConfig::class, UserRepositoryImpl::class])
internal class UserRepositoryTest(
    private val queryFactory: HibernateMutinyReactiveQueryFactory,
    private val userRepository: UserRepository
) {


    @Test
    fun saveEntity() = runTest {
        val user = saveUser()

        val userFromDb = queryFactory.transactionWithFactory { _, queryFactory ->
            queryFactory.singleQuery<User> {
                select(entity(User::class))
                from(entity(User::class))
                where(col(User::id).equal(user.id))
            }
        }

        EqualsTestUtil.isEqualTo(userFromDb, user)
    }

    @Test
    fun findByIdTest() = runTest {
        val user = saveUser()
        val userFromDB = userRepository.findByIdOrNull(user.id)
        EqualsTestUtil.isEqualTo(userFromDB!!, user)
    }

    @Test
    fun findByIdIfNullTest() = runTest {
        val userFromDB = userRepository.findByIdOrNull(UUID.randomUUID())
        assertThat(userFromDB).isNull()
    }

    private suspend fun saveUser(): User {
        val user = buildUser()

        queryFactory.transactionWithFactory { session, _ ->
            session.persist(user).awaitSuspending()
        }

        return user
    }

    private fun buildUser(): User {
        return User(
            name = "test",
            password = "testPassword",
            accountId = "accountId",
            birthDay = LocalDate.now(),
            classNum = 2,
            deviceToken = "sdaf",
            entranceYear = Year.of(2022),
            grade = 1,
            num = 2,
            profileFileName = "sdaf"
        )
    }

}