object Dependencies {
    val KTLINT by lazy { "com.pinterest:ktlint:${DependencyVersions.KTLINT_VERSION}" }
    val SPRING_CLOUD by lazy { "org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}" }
    val REACTIVE_JDSL by lazy { "com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:${DependencyVersions.JDSL_VERSION}" }
    val REACTIVE_MYSQL by lazy { "io.vertx:vertx-mysql-client:${DependencyVersions.REACTIVE_MYSQL_VERSION}" }
    val REACTIVE_HIBERNATE by lazy { "org.hibernate.reactive:hibernate-reactive-core:${DependencyVersions.HIBERNATE_REACTIVE_VERSION}" }
    val SPRING_DATA_COMMON by lazy { "org.springframework.data:spring-data-commons" }
    val MUTINY_KOTLIN by lazy { "io.smallrye.reactive:mutiny-kotlin:${DependencyVersions.MUTINY_KOTLIN_VERSION}" }
    val REACTIVE_DATA_REDIS by lazy { "org.springframework.boot:spring-boot-starter-data-redis-reactive" }
    val SPRING_SECURITY by lazy { "org.springframework.boot:spring-boot-starter-security" }
    val COROUTINE_REACTOR by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${DependencyVersions.COROUTINE_VERSION}" }
    val COROUTINE_JDK by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${DependencyVersions.COROUTINE_VERSION}" }
    val REACTOR_COROUTINE_EXTENSION by lazy { "io.projectreactor.kotlin:reactor-kotlin-extensions" }
    val WEBFLUX by lazy { "org.springframework.boot:spring-boot-starter-webflux" }
    val VALIDATION by lazy { "org.springframework.boot:spring-boot-starter-validation" }
    val JACKSON by lazy { "com.fasterxml.jackson.module:jackson-module-kotlin" }
    val AWS_MESSAGING by lazy { "org.springframework.cloud:spring-cloud-starter-aws-messaging" }
    val KOTLIN_REFLECT by lazy { "org.jetbrains.kotlin:kotlin-reflect" }
    val KOTLIN_STDLIB by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8" }
    val CONFIGURATION_PROCESSOR by lazy { "org.springframework.boot:spring-boot-configuration-processor" }
    val EMBEDDED_MYSQL by lazy { "com.wix:wix-embedded-mysql:${DependencyVersions.EMBEDDED_MYSQL_VERSION}" }
    val SPRING_TEST by lazy { "org.springframework.boot:spring-boot-starter-test" }
    val SECURITY_TEST by lazy { "org.springframework.security:spring-security-test" }
    val REACTOR_TEST by lazy { "io.projectreactor:reactor-test" }
    val COROUTINE_TEST by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${DependencyVersions.COROUTINE_VERSION}" }
}