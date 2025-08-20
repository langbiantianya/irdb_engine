import com.google.protobuf.gradle.*

plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
}

group = "com.kxxnzstdsw"
version = "1.0.0"

dependencies {
    implementation(project(":database"))
    implementation(project(":core"))
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koinExt)
    implementation(libs.kotlin.logging)
    api(libs.bundles.grpcExt)
    runtimeOnly(libs.logback.classic)
    protobuf(files("ext/"))
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
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
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }

}
tasks.test {
    useJUnitPlatform()
}