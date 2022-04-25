//package com.xquare.v1userservice.domain.repository
//
//import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
//import com.linecorp.kotlinjdsl.querydsl.expression.col
//import com.linecorp.kotlinjdsl.selectQuery
//import com.xquare.v1userservice.EmbeddedMySQLConfiguration
//import com.xquare.v1userservice.EqualsTestUtil
//import com.xquare.v1userservice.configuration.cdc.OutboxEntity
//import com.xquare.v1userservice.configuration.datasource.QueryBuilderConfig
//import com.xquare.v1userservice.user.User
//import com.xquare.v1userservice.user.UserState
//import com.xquare.v1userservice.user.mapper.UserDomainMapperImpl
//import com.xquare.v1userservice.user.repository.UserRepositoryImpl
//import com.xquare.v1userservice.user.saveuser.spi.UserRepositorySpi
//import java.time.LocalDate
//import java.time.Year
//import java.util.UUID
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertDoesNotThrow
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.TestConstructor
//
//@ExperimentalCoroutinesApi
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@SpringBootTest(
//    classes = [
//        EmbeddedMySQLConfiguration::class,
//        QueryBuilderConfig::class,
//        UserDomainMapperImpl::class,
//        UserRepositoryImpl::class
//    ]
//)
//internal class UserRepositoryImplTest(
//    private val userRepository: UserRepositorySpi,
//    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory
//) {
//
//    @Test
//    fun saveEntity() = runTest {
//        val user = assertDoesNotThrow { saveUser() }
//        assertThat(
//            reactiveQueryFactory.withFactory { _, springDataReactiveQueryFactory ->
//                springDataReactiveQueryFactory.selectQuery<OutboxEntity> {
//                    select(entity(OutboxEntity::class))
//                    from(entity(OutboxEntity::class))
//                    where(col(OutboxEntity::aggregateId).equal(user.id))
//                }.singleResultOrNull()
//            }
//        ).isNotNull
//    }
//
//    @Test
//    fun findByIdTest() = runTest {
//        val user = saveUser()
//        val userFromDB = userRepository.findByIdAndStateOrNull(user.id, UserState.CREATE_PENDING)
//        EqualsTestUtil.isEqualTo(userFromDB!!, user)
//    }
//
//    @Test
//    fun findByIdIfNullTest() = runTest {
//        val userFromDB = userRepository.findByIdAndStateOrNull(UUID.randomUUID(), UserState.CREATE_PENDING)
//        assertThat(userFromDB).isNull()
//    }
//
//    private suspend fun saveUser(): User {
//        val user = buildUser()
//        return userRepository.saveUserAndOutbox(user)
//    }
//
//    private fun buildUser(): User {
//        return User(
//            name = "test",
//            password = "testPassword",
//            accountId = "accountId",
//            birthDay = LocalDate.now(),
//            classNum = 2,
//            deviceToken = "sdaf",
//            entranceYear = Year.of(2022).value,
//            grade = 1,
//            num = 2,
//            profileFileName = "sdaf"
//        )
//    }
//}
