package com.kxxnzstdsw

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.BindableService
import io.grpc.ManagedChannelBuilder
import io.grpc.netty.NettyServerBuilder
import java.net.InetSocketAddress

private val logger = KotlinLogging.logger {}

fun grpcServer(startToken: String,bindableServices: List<BindableService>) {
    NettyServerBuilder.forAddress(InetSocketAddress("127.0.0.1", 50051)).apply {
        bindableServices.forEach {
            logger.info { "gRPC service registered: ${it.javaClass.simpleName}" }
            addService(it)
        }
        intercept(AuthInterceptor(startToken))
    }.build().start()
}