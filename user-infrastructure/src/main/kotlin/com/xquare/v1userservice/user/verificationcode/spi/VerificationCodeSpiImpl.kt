package com.xquare.v1userservice.user.verificationcode.spi

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.VerificationCodeEntity
import com.xquare.v1userservice.user.verificationcode.mapper.VerificationCodeMapper
import org.springframework.stereotype.Repository

@Repository
class VerificationCodeSpiImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory,
    private val verificationCodeMapper: VerificationCodeMapper
) : VerificationCodeSpi {
    override suspend fun getByCode(code: String): VerificationCode? {
        val verificationCodeEntityOrNull = reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.selectQuery<VerificationCodeEntity> {
                select(entity(VerificationCodeEntity::class))
                from(entity(VerificationCodeEntity::class))
                where(col(VerificationCodeEntity::code).equal(code))
            }.singleResultOrNull()
        }

        return verificationCodeEntityOrNull?.let { verificationCodeMapper.verificationCodeEntityToDomain(it) }
    }
}
