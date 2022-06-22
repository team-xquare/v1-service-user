package com.xquare.v1userservice.verificationcode.spi

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import com.xquare.v1userservice.user.verificationcode.VerificationCode
import com.xquare.v1userservice.user.verificationcode.spi.VerificationCodeSpi
import org.springframework.stereotype.Repository

@Repository
class VerificationCodeSpiImpl(
    private val reactiveQueryFactory: HibernateMutinyReactiveQueryFactory,
) : VerificationCodeSpi {
    override suspend fun getByCode(code: String): VerificationCode? {
        return reactiveQueryFactory.withFactory { _, reactiveQueryFactory ->
            reactiveQueryFactory.selectQuery<VerificationCode> {
                select(entity(VerificationCode::class))
                from(entity(VerificationCode::class))
                where(col(VerificationCode::code).equal(code))
            }.singleResultOrNull()
        }
    }
}
