package com.xquare.v1userservice.utils

import kotlin.reflect.KCallable
import kotlin.reflect.full.callSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T, PARAM, REVERT_PARAM> CoroutineScope.processAndRevertSteps(
    processStep: Pair<KCallable<T>, PARAM>,
    revertSteps: List<Pair<KCallable<Unit>, REVERT_PARAM>>,
): T = try {
    withContext(Dispatchers.IO) {
        processStep.first.callSuspend(processStep.second)
    }
} catch (e: Exception) {
    revertSteps.forEach { it.first.callSuspend(this, it.second) }
    throw e
}
