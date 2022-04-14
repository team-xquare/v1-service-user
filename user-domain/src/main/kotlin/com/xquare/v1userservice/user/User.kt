package com.xquare.v1userservice.user

import com.xquare.v1userservice.annotations.Aggregate
import java.time.LocalDate
import java.time.Year
import java.util.*

// TODO NEED TO USE STATE MACHINE
@Aggregate
class User(
    val name: String,

    val entranceYear: Year,

    val birthDay: LocalDate,

    val grade: Int,

    val profileFileName: String,

    val password: String,

    val accountId: String,

    classNum: Int,

    num: Int,

    deviceToken: String? = null,

    val id: UUID = UUID.randomUUID()
) {

    var classNum = classNum
        private set

    var num = num
        private set

    var deviceToken = deviceToken
        private set
}
