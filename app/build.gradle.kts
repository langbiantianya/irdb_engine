import com.google.protobuf.gradle.id

plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.protobuf)
    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlin.logging)
    implementation(libs.bundles.grpcExt)
    runtimeOnly(libs.logback.classic)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "com.kxxnzstdsw.app.AppKt"
}

protobuf {
    protoc {
        artifact = "${libs.protoc.lib.get().group}:${libs.protoc.lib.get().name}:${libs.protoc.lib.get().version}"
    }
    plugins {
        id("grpc") {

            artifact =
                "${libs.protoc.gen.grpc.java.get().group}:${libs.protoc.gen.grpc.java.get().name}:${libs.protoc.gen.grpc.java.get().version}"
        }
        id("grpckt") {
            artifact =
                "${libs.protoc.gen.grpc.kotlin.get().group}:${libs.protoc.gen.grpc.kotlin.get().name}:${libs.protoc.gen.grpc.kotlin.get().version}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }

    sourceSets {
        main {
            proto {
                srcDir("src/main/resources/protos") // 模块下的proto文件夹
//        include("**/*.proto")
            }
        }
    }

}
