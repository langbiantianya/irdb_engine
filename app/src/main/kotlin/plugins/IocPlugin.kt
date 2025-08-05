package com.kxxnzstdsw.app.plugins

import org.koin.core.context.GlobalContext.startKoin

fun initIoc() {
    startKoin {
        modules()
    }
}