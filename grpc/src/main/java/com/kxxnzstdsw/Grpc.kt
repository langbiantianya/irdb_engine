package com.kxxnzstdsw

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.BindableService
import io.grpc.Server
import io.grpc.netty.NettyServerBuilder
import java.net.InetSocketAddress

private val logger = KotlinLogging.logger {}

private lateinit var server: Server

fun grpcServer(startToken: String, bindableServices: List<BindableService>) {
    server = NettyServerBuilder.forAddress(InetSocketAddress("127.0.0.1", 50051)).apply {
        bindableServices.forEach {
            logger.info { "gRPC service registered: ${it.javaClass.simpleName}" }
            addService(it)
        }
        intercept(AuthInterceptor(startToken))
    }.build().start()
}
fun grpcServerShutdown() {
    server.shutdown()
}
