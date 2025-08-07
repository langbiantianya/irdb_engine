

plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.ksp)
    alias(libs.plugins.shadow)
    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

group = "com.kxxnzstdsw"
version = "1.0.0"

dependencies {
    implementation(project(":utils"))
    implementation(project(":grpc"))
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koinExt)
    implementation(libs.kotlin.logging)
    runtimeOnly(libs.logback.classic)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "com.kxxnzstdsw.AppKt"
}

tasks.test {
    useJUnitPlatform()
}
