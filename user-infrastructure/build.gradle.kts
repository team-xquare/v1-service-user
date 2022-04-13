plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencyManagement {
    imports {
        mavenBom(Dependencies.SPRING_CLOUD)
    }
}

dependencies {
    implementation(Dependencies.REACTIVE_HIBERNATE)
    implementation(Dependencies.REACTIVE_JDSL)
    implementation(Dependencies.SPRING_DATA_COMMON)
    implementation(Dependencies.REACTIVE_MYSQL)
    implementation(Dependencies.REACTIVE_DATA_REDIS)
    implementation(Dependencies.MUTINY_KOTLIN)
    implementation(Dependencies.MUTINY_REACTOR)
    implementation(Dependencies.COROUTINE_REACTOR)
    implementation(Dependencies.COROUTINE_JDK)
    implementation(Dependencies.REACTOR_COROUTINE_EXTENSION)
    implementation(Dependencies.WEBFLUX)
    implementation(Dependencies.VALIDATION)
    implementation(Dependencies.JACKSON)
    implementation(Dependencies.AWS_MESSAGING)
    implementation(Dependencies.ZIPKIN)
    implementation(Dependencies.SPRING_RABBIT)
    implementation(Dependencies.STARTER_SLEUTH)
    implementation(Dependencies.ACTUATOR)
    implementation(Dependencies.MICROMETER)
    annotationProcessor(Dependencies.CONFIGURATION_PROCESSOR)
    testImplementation(Dependencies.SPRING_TEST)
    testImplementation(Dependencies.REACTOR_TEST)
    testImplementation(Dependencies.COROUTINE_TEST)
    testImplementation(Dependencies.EMBEDDED_MYSQL)
    implementation(Dependencies.MAPSTRUCT)
    implementation(Dependencies.SPRING_SECURITY)
    kapt(Dependencies.MAPSTRUCT_APT)
    testImplementation(Dependencies.MOCKITO_KOTLIN)
    testImplementation(Dependencies.MOCKITO_INLINE)

    implementation(project(":user-domain"))
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "error")
    }
}


allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}