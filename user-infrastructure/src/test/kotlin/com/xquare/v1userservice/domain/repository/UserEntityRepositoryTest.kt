package com.xquare.v1userservice.domain.repository

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.xquare.v1userservice.EmbeddedMySQLConfiguration
import com.xquare.v1userservice.EqualsTestUtil
import com.xquare.v1userservice.configuration.datasource.QueryBuilderConfig
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.mapper.UserDomainMapperImpl
import com.xquare.v1userservice.user.repository.UserRepositoryImpl
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate
import java.time.Year
import java.util.*

@ExperimentalCoroutinesApi
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(
    classes = [
        EmbeddedMySQLConfiguration::class,
        QueryBuilderConfig::class,
        UserDomainMapperImpl::class,
        UserRepositoryImpl::class
    ]
)
internal class UserEntityRepositoryTest(
    private val userRepository: UserRepositorySpi,
    private val queryFactory: HibernateMutinyReactiveQueryFactory
) {

    @Test
    fun saveEntity() = runTest {
        assertDoesNotThrow { saveUser() }
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
        return userRepository.save(user)
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