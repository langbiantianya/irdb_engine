package com.kxxnzstdsw.plugins


import com.kxxnzstdsw.GrpcModules
import org.koin.core.context.GlobalContext


fun startIoc() {
    GlobalContext.stopKoin()
    GlobalContext.startKoin {
        printLogger()
        modules(
            GrpcModules.grpcModule
        )
    }
}

fun koin() = GlobalContext.get()
