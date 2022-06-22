package com.xquare.v1userservice.verificationcode

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

    @NotNull
    @Column(unique = true)
    val code: String,

    @NotNull
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    val isUsed: Boolean,

    @NotNull
    val expiredAt: LocalDate,

    @NotNull
    val userName: String,

    @NotNull
    @Column(columnDefinition = "TINYINT(1)")
    val grade: Int,

    @NotNull
    @Column(columnDefinition = "TINYINT(1)")
    val classNum: Int,

    @NotNull
    @Column(columnDefinition = "TINYINT(2)")
    val num: Int,

    @NotNull
    val birthDay: LocalDate,

    @NotNull
    @Column(columnDefinition = "TINYINT(2)")
    val entranceYear: Int
)
