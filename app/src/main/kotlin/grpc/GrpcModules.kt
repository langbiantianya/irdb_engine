package com.kxxnzstdsw.app.grpc

import com.kxxnzstdsw.app.grpc.server.HelloWorldService
import io.grpc.BindableService
import org.koin.dsl.module

object GrpcModules {
    val grpcModule = module {
        single<BindableService> {
            HelloWorldService()
        }
    }
}