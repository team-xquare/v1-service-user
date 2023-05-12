package com.xquare.v1userservice.configuration.extension

/**
 * This is a Kotlin extension function for the List class that checks if all the elements in the list are either null or blank.
 *
 * @return null if all the elements in the list are either null or blank. Otherwise, it returns the original list.
 */
fun List<String?>.nullIfBlank(): List<String?>? =
    if (this.all { it.isNullOrBlank() }) null else this
