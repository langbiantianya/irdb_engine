package com.kxxnzstdsw

object Engine {
    @get:Synchronized
    @set:Synchronized
    var appExit = false
        get() {
            val tmp = field
            field = false
            return tmp
        }
    @get:Synchronized
    @set:Synchronized
    var appRestart = false
        get() {
            val tmp = field
            field = false
            return tmp
        }

    fun exit() {
        appExit = true
    }

    fun restart() {
        appExit = true
        appRestart = true
    }
}