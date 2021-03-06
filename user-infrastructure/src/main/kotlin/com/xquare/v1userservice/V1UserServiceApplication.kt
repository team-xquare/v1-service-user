package com.xquare.v1userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

internal const val BASE_PACKAGE = "com.xquare.v1userservice"

@SpringBootApplication
class V1UserServiceApplication

fun main(args: Array<String>) {
    runApplication<V1UserServiceApplication>(*args)
}
