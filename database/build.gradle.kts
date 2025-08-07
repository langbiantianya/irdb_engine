plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.ksp)
}

group = "com.kxxnzstdsw"
version = "1.0.0"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koinExt)
    implementation(libs.kotlin.logging)
    implementation(libs.bundles.database)
    runtimeOnly(libs.logback.classic)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
}

tasks.test {
    useJUnitPlatform()
}