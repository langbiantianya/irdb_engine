package com.kxxnzstdsw.app

import com.kxxnzstdsw.app.plugins.grpcServer

import com.kxxnzstdsw.app.plugins.koin
import com.kxxnzstdsw.app.plugins.logger
import com.kxxnzstdsw.app.plugins.startIoc
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
fun main() {
    try {
        startIoc()
        grpcServer(koin().get())
    } catch (e: Exception) {
        logger.error { e }
        return
    }

    logger.info { "启动成功" }
    while (true){}
}
