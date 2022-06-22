package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserRole
import com.xquare.v1userservice.user.api.CreateUserApi
import com.xquare.v1userservice.user.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.spi.PasswordEncoderSpi
import com.xquare.v1userservice.user.spi.SaveUserBaseAuthoritySpi
import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.spi.VerificationCodeSpi

@DomainService
class CreateUserApiImpl(
    private val createUserInPendingStateProcessor: CreateUserInPendingStateProcessor,
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor,
    private val saveUserBaseAuthoritySpi: SaveUserBaseAuthoritySpi,
    private val verificationCodeSpi: VerificationCodeSpi,
    private val passwordEncoderSpi: PasswordEncoderSpi
) : CreateUserApi {
    override suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User {
        val verificationCode = verificationCodeSpi.getByCode(creatUserDomainRequest.verificationCode) ?: TODO()
        val domainUser = verificationCode.toStudentUser(creatUserDomainRequest)
        val savedUser = createUserInPendingStateProcessor.processStep(domainUser)
        saveUserBaseAuthoritySpi.processStep(savedUser.id)
        updateUserCreatedStateStepProcessor.processStep(savedUser.id)
        return savedUser
    }

    private fun VerificationCode.toStudentUser(creatUserDomainRequest: CreatUserDomainRequest) = User(
        name = this.userName,
        entranceYear = this.entranceYear,
        birthDay = this.birthDay,
        grade = this.grade,
        role = UserRole.STU,
        num = this.num,
        classNum = this.classNum,
        password = passwordEncoderSpi.encodeString(creatUserDomainRequest.password),
        accountId = creatUserDomainRequest.accountId,
        profileFileName = creatUserDomainRequest.profileFileName
    )
}
