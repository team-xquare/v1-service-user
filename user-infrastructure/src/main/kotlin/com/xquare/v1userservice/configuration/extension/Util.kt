package com.xquare.v1userservice.configuration.extension

import java.util.UUID

/**
 * This is a Kotlin extension function for the List class that checks if all the elements in the list are either null or blank.
 *
 * @return null if all the elements in the list are either null or blank. Otherwise, it returns the original list.
 */
fun List<UUID>.nullIfBlank(): List<UUID>? =
    if (this.all { it.toString().isBlank() }) null else this
