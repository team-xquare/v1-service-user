package com.xquare.v1userservice.configuration.cdc

class DebeziumMessage(
    val after: After
)

class After(
    val payload: String
)
