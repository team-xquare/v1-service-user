package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Spi
import java.util.UUID

@Spi
interface SaveUserBaseApplicationProcessor {
    suspend fun processStep(userId: UUID)
}
