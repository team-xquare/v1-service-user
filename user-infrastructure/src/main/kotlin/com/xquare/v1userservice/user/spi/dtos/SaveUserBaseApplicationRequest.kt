package com.xquare.v1userservice.user.spi.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class SaveUserBaseApplicationRequest(
    @JsonProperty("user_id")
    val userId: UUID
)
