package com.kxxnzstdsw.app.grpc.server

import io.grpc.examples.helloworld.GreeterGrpcKt
import io.grpc.examples.helloworld.HelloRequest
import io.grpc.examples.helloworld.helloReply


class HelloWorldService : GreeterGrpcKt.GreeterCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest) =
        helloReply {
            message = "Hello ${request.name}"
        }
}