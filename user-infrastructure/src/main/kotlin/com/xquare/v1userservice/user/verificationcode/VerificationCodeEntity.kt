package com.xquare.v1userservice.user.verificationcode

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_verification_code")
class VerificationCodeEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @field:NotNull
    @Column(unique = true)
    val code: String,

    @field:NotNull
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    val isUsed: Boolean,

    @field:NotNull
    val expiredAt: LocalDate,

    @field:NotNull
    val userName: String,

    @field:NotNull
    @Column(columnDefinition = "TINYINT(1)")
    val grade: Int,

    @field:NotNull
    @Column(columnDefinition = "TINYINT(1)")
    val classNum: Int,

    @field:NotNull
    @Column(columnDefinition = "TINYINT(2)")
    val num: Int,

    @field:NotNull
    val birthDay: LocalDate,

    @NotNull
    @Column(columnDefinition = "TINYINT(2)")
    val entranceYear: Int
)
