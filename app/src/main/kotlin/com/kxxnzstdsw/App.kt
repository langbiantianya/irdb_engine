package com.kxxnzstdsw

import com.kxxnzstdsw.plugins.koin
import com.kxxnzstdsw.plugins.startIoc
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

var appExit = false
fun main(args: Array<String>) {
//    arags 中获取 -token/-T
    val argMap = args.toList()
        .windowed(2)                     // 两两成对
        .associate { it[0] to it[1] }   // ["-T", "abc"] -> mapOf("-T" to "abc")

    val token = argMap["-token"] ?: argMap["-T"]
    if (token.isNullOrBlank()) {
        logger.error { "token 不能为空" }
        return
    }

    try {
        startIoc()
        grpcServer(token, koin().get())
    } catch (e: Exception) {
        logger.error { e }
        return
    }

    logger.info { "启动成功" }
    while (!appExit) {
    }

}
