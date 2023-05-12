package com.xquare.v1userservice.configuration.extension

fun List<String?>.ifContentEmpty(): List<String?>? =
    if (this.all { it.isNullOrBlank() }) null else this
