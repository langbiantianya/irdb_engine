package com.kxxnzstdsw

import com.kxxnzstdsw.server.HelloWorldService
import com.kxxnzstdsw.server.SessionService
import io.grpc.BindableService
import org.koin.dsl.module

object GrpcModules {
    val grpcModule = module {
        single<List<BindableService>> {
            listOf(HelloWorldService(), SessionService())
        }
    }
}