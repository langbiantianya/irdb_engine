package com.kxxnzstdsw.app

import com.kxxnzstdsw.app.plugins.initIoc
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val logger = KotlinLogging.logger {}
    try {
        initIoc()
    } catch (e: Exception) {
        logger.error { e }
        return
    }
    logger.info { "启动成功" }
}
