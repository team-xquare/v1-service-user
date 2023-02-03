package com.xquare.v1userservice.user

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_user")
class UserEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @field:NotNull
    @field:Size(max = 5)
    val name: String,

    @field:NotNull
    val entranceYear: Int,

    @field:NotNull
    val birthDay: LocalDate,

    @field:NotNull
    val grade: Int,

    @field:Size(max = 2000)
    val profileFileName: String?,

    @field:NotNull
    val password: String,

    @field:NotNull
    @Column(nullable = false, unique = true)
    val accountId: String,

    classNum: Int,

    num: Int,

    state: UserState,

    deviceToken: String?,

    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    val role: UserRole
) {
    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    var state: UserState = state
        protected set

    @field:NotNull
    var classNum = classNum
        protected set

    @field:NotNull
    var num = num
        protected set

    var deviceToken = deviceToken
        protected set
}
