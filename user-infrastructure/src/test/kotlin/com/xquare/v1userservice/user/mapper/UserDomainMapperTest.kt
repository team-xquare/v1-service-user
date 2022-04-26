package com.xquare.v1userservice.user.mapper

import com.xquare.v1userservice.EqualsTestUtils
import com.xquare.v1userservice.UserUtils
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.mockito.Spy


internal class UserDomainMapperTest {

    @Spy
    private val userMapper = Mappers.getMapper(UserDomainMapper::class.java)

    @Test
    fun entityToDomain() {
        val userEntity = UserUtils.buildUserEntityWithCreatePendingState()
        val userDomainObject = userMapper.userEntityToDomain(userEntity)
        EqualsTestUtils.isEqualTo(userEntity, userDomainObject)
    }

    @Test
    fun domainToEntity() {
        val userDomainObject = UserUtils.buildUserWithCreatePendingState()
        val userEntity = userMapper.userDomainToEntity(userDomainObject)
        EqualsTestUtils.isEqualTo(userEntity, userDomainObject)
    }

}