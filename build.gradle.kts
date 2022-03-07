import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("jvm") version PluginVersions.JVM_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(Dependencies.KTLINT) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

dependencyManagement {
    imports {
        mavenBom(Dependencies.SPRING_CLOUD)
    }
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
    implementation(Dependencies.R2DBC)
    implementation(Dependencies.REACTIVE_MYSQL)
    implementation(Dependencies.REACTIVE_DATA_Redis)
    implementation(Dependencies.SPRING_SECURITY)
    implementation(Dependencies.COROUTINE_REACTOR)
    implementation(Dependencies.REACTOR_COROUTINE_EXTENSION)
    implementation(Dependencies.WEBFLUX)
    implementation(Dependencies.VALIDATION)
    implementation(Dependencies.JACKSON)
    implementation(Dependencies.AWS_MESSAGING)
    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.KOTLIN_REFLECT)
    annotationProcessor(Dependencies.CONFIGURATION_PROCESSOR)
    testImplementation(Dependencies.SPRING_TEST)
    testImplementation(Dependencies.SECURITY_TEST)
    testImplementation(Dependencies.REACTOR_TEST)
    testRuntimeOnly(Dependencies.H2_DRIVER)
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

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

// Checking lint
val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/**/*.kt")
}

// Formatting all source files
val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("-F", "src/**/*.kt")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

val installGitHook by tasks.creating(Copy::class) {

    description = "Install git hook to root project."

    var suffix = "macos"
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        suffix = "windows"
    }

    val sourceDir = File(rootProject.rootDir, "pre-build/scripts/pre-push-$suffix")
    val targetDir = File(rootProject.rootDir, ".git/hooks")

    from(sourceDir)
    into(targetDir)
    rename("pre-push-$suffix", "pre-push")

    fileMode = 0b111101101
}

project.tasks
    .getByName("build")
    .dependsOn(":installGitHook")