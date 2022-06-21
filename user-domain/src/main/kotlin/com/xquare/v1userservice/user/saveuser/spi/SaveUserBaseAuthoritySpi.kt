package com.xquare.v1userservice.user.saveuser.spi

import com.xquare.v1userservice.annotations.Spi
import java.util.UUID

@Spi
interface SaveUserBaseAuthoritySpi {
    suspend fun processStep(userId: UUID)
}
