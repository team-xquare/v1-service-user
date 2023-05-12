package com.xquare.v1userservice.configuration.extension

fun List<String?>.nullIfBlank(): List<String?>? =
    if (this.all { it.isNullOrBlank() }) null else this
