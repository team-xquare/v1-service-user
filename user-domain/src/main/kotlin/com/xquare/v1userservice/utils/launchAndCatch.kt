package com.xquare.v1userservice.utils

import kotlin.reflect.KCallable
import kotlin.reflect.full.callSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> processAndRevertSteps(
    processStep: Pair<KCallable<T>, Array<Any>>,
    revertSteps: List<Pair<KCallable<Unit>, Array<Any>>>,
): T = try {
    withContext(Dispatchers.IO) {
        processStep.first.callSuspend(processStep.second)
    }
} catch (e: Exception) {
    revertSteps.forEach { it.first.callSuspend(it.second) }
    throw e
}
