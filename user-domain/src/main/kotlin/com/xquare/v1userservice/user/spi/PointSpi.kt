package com.xquare.v1userservice.user.spi

import com.xquare.v1userservice.annotations.Spi
import com.xquare.v1userservice.user.spi.dtos.PointResponse
import java.util.UUID

@Spi
interface PointSpi {
    suspend fun getUserPoint(userId: UUID): PointResponse

    suspend fun saveUserPointStatus(userId: UUID)
}
