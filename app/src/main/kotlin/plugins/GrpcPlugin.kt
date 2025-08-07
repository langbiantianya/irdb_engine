package com.kxxnzstdsw.app.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.BindableService
import io.grpc.ServerBuilder

private val logger = KotlinLogging.logger {}

fun grpcServer(vararg bindableServices: BindableService) {
    ServerBuilder
        .forPort(9000)
        .apply {
            bindableServices.forEach {
                logger.info { "gRPC service registered: ${it.javaClass.simpleName}" }
                addService(it)
            }
        }
        .build()
        .start()
}