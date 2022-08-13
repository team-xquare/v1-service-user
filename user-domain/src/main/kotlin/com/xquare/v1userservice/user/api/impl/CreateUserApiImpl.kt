package com.xquare.v1userservice.user.api.impl

import com.xquare.v1userservice.annotations.DomainService
import com.xquare.v1userservice.user.User
import com.xquare.v1userservice.user.UserRole
import com.xquare.v1userservice.user.api.CreateUserApi
import com.xquare.v1userservice.user.api.CreateUserInPendingStateCompensator
import com.xquare.v1userservice.user.api.CreateUserInPendingStateProcessor
import com.xquare.v1userservice.user.api.UpdateUserCreatedStateStepProcessor
import com.xquare.v1userservice.user.api.dtos.CreatUserDomainRequest
import com.xquare.v1userservice.user.exceptions.UserAlreadyExistsException
import com.xquare.v1userservice.user.spi.PasswordEncoderSpi
import com.xquare.v1userservice.user.spi.SaveUserBaseApplicationCompensator
import com.xquare.v1userservice.user.spi.SaveUserBaseAuthorityCompensator
import com.xquare.v1userservice.user.spi.SaveUserBaseAuthorityProcessor
import com.xquare.v1userservice.user.spi.UserRepositorySpi
import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.exceptions.VerificationCodeNotFoundException
import com.xquare.v1userservice.user.verificationcode.spi.VerificationCodeSpi
import com.xquare.v1userservice.utils.processAndRevert
import kotlinx.coroutines.coroutineScope

@DomainService
class CreateUserApiImpl(
    private val createUserInPendingStateProcessor: CreateUserInPendingStateProcessor,
    private val updateUserCreatedStateStepProcessor: UpdateUserCreatedStateStepProcessor,
    private val saveUserBaseAuthorityProcessor: SaveUserBaseAuthorityProcessor,
    private val verificationCodeSpi: VerificationCodeSpi,
    private val passwordEncoderSpi: PasswordEncoderSpi,
    private val userRepositorySpi: UserRepositorySpi
) : CreateUserApi {
    override suspend fun saveUser(creatUserDomainRequest: CreatUserDomainRequest): User {
        val verificationCode = verificationCodeSpi.getByCode(creatUserDomainRequest.verificationCode)
            ?: throw VerificationCodeNotFoundException("Verification Code Not Found")
        checkIsAccountIdAlreadyExists(creatUserDomainRequest.accountId)
        val domainUser = verificationCode.toStudentUser(creatUserDomainRequest)
        val savedUser = createUserInPendingStateProcessor.processStep(domainUser)

        coroutineScope {
            processAndRevert(
                processSteps = saveUserBaseAuthorityProcessor::processStep,
                processStepParam = savedUser.id,
                revertSteps = listOf(
                    SaveUserBaseAuthorityCompensator::revertStep to savedUser.id,
                    CreateUserInPendingStateCompensator::revertStep to savedUser.id
                )
            )

            processAndRevert(
                processSteps = saveUserBaseAuthorityProcessor::processStep,
                processStepParam = savedUser.id,
                revertSteps = listOf(
                    SaveUserBaseAuthorityCompensator::revertStep to savedUser.id,
                    SaveUserBaseApplicationCompensator::revertStep to savedUser.id,
                    CreateUserInPendingStateCompensator::revertStep to savedUser.id
                )
            )
        }

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

    private suspend fun checkIsAccountIdAlreadyExists(accountId: String) {
        userRepositorySpi.findByAccountIdAndStateWithCreated(accountId)
            ?.let { throw UserAlreadyExistsException("$accountId Already Exists") }
    }
}
