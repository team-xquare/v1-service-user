package com.xquare.v1userservice

import org.assertj.core.api.Assertions.assertThat
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

object EqualsTestUtil {
    fun isEqualTo(baseObject: Any, targetObject: Any) {
        val baseObjectFieldMap = baseObject::class.memberProperties
            .associateBy { it.name }

        val targetObjectFields = targetObject::class.memberProperties

        targetObjectFields
            .forEach {
                assertThat(getValueOfProperty(baseObject, baseObjectFieldMap[it.name]!!))
                    .isEqualTo(getValueOfProperty(targetObject, it))
            }
    }

    private fun getValueOfProperty(obj: Any, property: KProperty<*>) =
        property.getter.call(obj)
}
