package com.xquare.v1userservice.configuration.cdc

import io.vertx.core.json.JsonObject
import java.sql.Timestamp
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_outbox")
class OutboxEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @field:NotNull
    @Column(columnDefinition = "BINARY(16)")
    val aggregateId: UUID,

    @field:NotNull
    val aggregateType: String,

    @field:NotNull
    @Column(columnDefinition = "json")
    val payload: JsonObject,

    @field:NotNull
    val timestamp: Timestamp,

    @field:NotNull
    val type: String
)
