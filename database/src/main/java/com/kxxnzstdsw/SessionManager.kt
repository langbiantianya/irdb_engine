package com.kxxnzstdsw

import com.kxxnzstdsw.model.DatabaseInfo
import kotlinx.coroutines.runBlocking

object SessionManager {
    private val sessionMap = mutableMapOf<String, Database>()

    @Synchronized
    fun createSession(name: String, uri: String) {
        sessionMap[name] = Database(name, uri)
    }

    fun getSessionInfo(name: String): DatabaseInfo? {
        return sessionMap[name]?.let {
            return DatabaseInfo(runBlocking { it.databaseVersion() }, it.driverName)
        }
    }
}