package com.kxxnzstdsw.plugin

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.BindableService
import io.grpc.netty.NettyServerBuilder
import java.net.InetSocketAddress

private val logger = KotlinLogging.logger {}

fun grpcServer(bindableServices: List<BindableService>) {
    NettyServerBuilder.forAddress(InetSocketAddress("127.0.0.1", 50051)).apply {
        bindableServices.forEach {
            logger.info { "gRPC service registered: ${it.javaClass.simpleName}" }
            addService(it)
        }
    }
        .build()
        .start()
}