package com.xquare.v1userservice.user.mapper

import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserEntity
import org.mapstruct.Mapper

@Mapper
interface UserDomainMapper {
    fun userDomainToEntity(user: User): UserEntity
    fun userEntityToDomain(userEntity: UserEntity): User
}
