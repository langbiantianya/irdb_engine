package com.kxxnzstdsw.app.plugins


import com.kxxnzstdsw.app.grpc.GrpcModules
import org.koin.core.context.GlobalContext


fun startIoc() {
    GlobalContext.startKoin {
        printLogger()
        modules(
            GrpcModules.grpcModule
        )
    }
}

fun koin() = GlobalContext.get()
