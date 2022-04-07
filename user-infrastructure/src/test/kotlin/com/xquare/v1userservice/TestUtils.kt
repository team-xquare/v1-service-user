package com.xquare.v1userservice

import org.assertj.core.api.Assertions.assertThat
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

class TestUtils {
    companion object {
        fun isPropertyNotNull(obj: Any, vararg nullProperties: KProperty<*>) {

            val values = obj::class.memberProperties
                .filter { property -> nullProperties.none { nullProperty -> property == nullProperty } }
                .map { it.getter.call(obj) }

            values.forEach { value -> assertThat(value).isNotNull }
        }

//        private fun hasProperty(properties: Array<KProperty>) {
//
//        }
    }
}