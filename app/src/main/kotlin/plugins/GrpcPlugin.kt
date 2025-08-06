package com.kxxnzstdsw.app.plugins

import io.grpc.BindableService
import io.grpc.ServerBuilder


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