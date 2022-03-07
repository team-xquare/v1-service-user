object Dependencies {
    val KTLINT by lazy { "com.pinterest:ktlint:${DependencyVersions.KTLINT_VERSION}" }
    val SPRING_CLOUD by lazy { "org.springframework.cloud:spring-cloud-dependencies:${DependencyVersions.SPRING_CLOUD_VERSION}" }
    val REACTIVE_MYSQL by lazy { "com.github.jasync-sql:jasync-mysql:${DependencyVersions.REACTIVE_MYSQL_VERSION}" }
    val R2DBC by lazy { "org.springframework.boot:spring-boot-starter-data-r2dbc" }
    val REACTIVE_DATA_Redis by lazy { "org.springframework.boot:spring-boot-starter-data-redis-reactive" }
    val SPRING_SECURITY by lazy { "org.springframework.boot:spring-boot-starter-security" }
    val COROUTINE_REACTOR by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-reactor" }
    val REACTOR_COROUTINE_EXTENSION by lazy { "io.projectreactor.kotlin:reactor-kotlin-extensions" }
    val WEBFLUX by lazy { "org.springframework.boot:spring-boot-starter-webflux" }
    val VALIDATION by lazy { "org.springframework.boot:spring-boot-starter-validation" }
    val JACKSON by lazy { "com.fasterxml.jackson.module:jackson-module-kotlin" }
    val AWS_MESSAGING by lazy { "org.springframework.cloud:spring-cloud-starter-aws-messaging" }
    val KOTLIN_REFLECT by lazy { "org.jetbrains.kotlin:kotlin-reflect" }
    val KOTLIN_STDLIB by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8" }
    val CONFIGURATION_PROCESSOR by lazy { "org.springframework.boot:spring-boot-configuration-processor" }
    val H2_DRIVER by lazy { "io.r2dbc:r2dbc-h2" }
    val SPRING_TEST by lazy { "org.springframework.boot:spring-boot-starter-test" }
    val SECURITY_TEST by lazy { "org.springframework.security:spring-security-test" }
    val REACTOR_TEST by lazy { "io.projectreactor:reactor-test" }
}