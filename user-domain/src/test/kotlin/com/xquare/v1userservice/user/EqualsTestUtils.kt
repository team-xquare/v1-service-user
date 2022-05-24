package com.xquare.v1userservice.user

import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
import org.assertj.core.api.Assertions.assertThat

object EqualsTestUtils {
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
