package com.kxxnzstdsw.app

import com.kxxnzstdsw.app.plugins.grpcServer
import com.kxxnzstdsw.app.plugins.koin
import com.kxxnzstdsw.app.plugins.startIoc
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

var appExit = false
fun main() {
    try {
        startIoc()
        grpcServer(koin().get())
    } catch (e: Exception) {
        logger.error { e }
        return
    }

    logger.info { "启动成功" }
    while (!appExit) {
    }

}
