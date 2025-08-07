package database

import com.kxxnzstdsw.app.database.Database
import com.kxxnzstdsw.app.plugins.logger
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test


internal class ConnectionPoolTest {
    val database = Database("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123456")

    @Test
    fun databasesPg() {
        val res = runBlocking { database.databases() }
        logger.info { "数据库列表: $res" }
    }

    @Test
    fun databaseVersionPg() {
        val res = runBlocking { database.databaseVersion() }
        logger.info { "数据库版本: $res" }
    }


    @Test
    fun usersPg() {
        val res = runBlocking { database.users() }
        logger.info { "数据库用户: $res" }
    }

    @Test
    fun rolesPg() {
        val res = runBlocking { database.roles() }
        logger.info { "数据库角色: $res" }
    }

    @Test
    fun tablesPg() {
        var res = runBlocking { database.tables() }
        logger.info { "数据库表: $res" }
        res = runBlocking { database.tables("public") }
        logger.info { "数据库表: $res" }
    }

    @Test
    fun tableSchemaPg() {
        val res = runBlocking { database.tableSchema("public.test_o1") }
        logger.info { "数据库表结构: $res" }
    }
}