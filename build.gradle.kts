import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "com.xquare"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    // MySQL driver
    implementation("com.github.jasync-sql:jasync-mysql:2.0.6")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // reactor
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // kotlin jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // AWS Messaging
    implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging")

    // base
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // configuration processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")

    // test h2
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
