package com.xquare.v1userservice.utils

import kotlin.reflect.KCallable
import kotlin.reflect.full.callSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T, PARAM, REVERT_PARAM> CoroutineScope.processAndRevert(
    processStepParam: PARAM,
    processSteps: KCallable<T>,
    revertSteps: List<Pair<KCallable<Unit>, REVERT_PARAM>>,
): T = try {
    withContext(Dispatchers.IO) {
        processSteps.callSuspend(processStepParam)
    }
} catch (e: Exception) {
    revertSteps.forEach { it.first.callSuspend(this, it.second) }
    throw e
}
