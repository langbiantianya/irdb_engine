package com.kxxnzstdsw

import com.kxxnzstdsw.plugin.grpcServer
import com.kxxnzstdsw.plugins.koin
import com.kxxnzstdsw.plugins.startIoc
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
