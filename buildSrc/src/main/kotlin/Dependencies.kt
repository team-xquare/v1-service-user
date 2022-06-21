object Dependencies {
    // ktlint
    const val KTLINT = "com.pinterest:ktlint:${DependencyVersions.KTLINT_VERSION}"

    // webflux
    const val WEBFLUX = "org.springframework.boot:spring-boot-starter-webflux"

    // validation
    const val VALIDATION = "org.springframework.boot:spring-boot-starter-validation"

    // kotlin
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin"

    // configuration processor
    const val CONFIGURATION_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor"

    // Coroutine & Reactor
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${DependencyVersions.COROUTINE_VERSION}"
    const val REACTOR_COROUTINE_EXTENSION = "io.projectreactor.kotlin:reactor-kotlin-extensions"
    const val COROUTINE_REACTOR = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${DependencyVersions.COROUTINE_VERSION}"
    const val COROUTINE_JDK = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${DependencyVersions.COROUTINE_VERSION}"
    const val KOTLINX_COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersions.COROUTINE_VERSION}"
    const val REACTOR_TEST = "io.projectreactor:reactor-test"

    // Actuator
    const val ACTUATOR = "org.springframework.boot:spring-boot-starter-actuator"
    const val MICROMETER = "io.micrometer:micrometer-registry-prometheus"

    // mapstruct
    const val MAPSTRUCT = "org.mapstruct:mapstruct:${DependencyVersions.MAPSTRUCT_VERSION}"
    const val MAPSTRUCT_APT = "org.mapstruct:mapstruct-processor:${DependencyVersions.MAPSTRUCT_VERSION}"

    // security
    const val SPRING_SECURITY = "org.springframework.boot:spring-boot-starter-security"
    const val SECURITY_JWT = "com.nimbusds:nimbus-jose-jwt:${DependencyVersions.NIMBUS_VERSION}"

    // test
    const val EMBEDDED_MYSQL = "com.wix:wix-embedded-mysql:${DependencyVersions.EMBEDDED_MYSQL_VERSION}"
    const val SPRING_TEST = "org.springframework.boot:spring-boot-starter-test:${PluginVersions.SPRING_BOOT_VERSION}"
    const val MOCKK = "io.mockk:mockk:${DependencyVersions.MOCKK_VERSION}"
    const val SPRING_MOCKK = "com.ninja-squad:springmockk:${DependencyVersions.SPRING_MOCKK_VERSION}"

    // jdsl
    const val MUTINY_KOTLIN = "io.smallrye.reactive:mutiny-kotlin:${DependencyVersions.MUTINY_VERSION}"
    const val MUTINY_REACTOR = "io.smallrye.reactive:mutiny-reactor:${DependencyVersions.MUTINY_VERSION}"
    const val REACTIVE_JDSL = "com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:${DependencyVersions.JDSL_VERSION}"
    const val REACTIVE_MYSQL = "io.vertx:vertx-mysql-client:${DependencyVersions.REACTIVE_MYSQL_VERSION}"
    const val REACTIVE_HIBERNATE = "org.hibernate.reactive:hibernate-reactive-core:${DependencyVersions.HIBERNATE_REACTIVE_VERSION}"
    const val SPRING_DATA_COMMON = "org.springframework.data:spring-data-commons"

    // cloud
    const val SPRING_CLOUD = "org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}"
}
