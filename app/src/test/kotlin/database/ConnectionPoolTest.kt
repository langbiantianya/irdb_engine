package database

import com.kxxnzstdsw.app.database.Database
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test


internal class ConnectionPoolTest {
    val database = Database("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123456")
    private val logger = KotlinLogging.logger {}

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
        var res = runBlocking { database.tables("public") }
        logger.info { "数据库表: $res" }
        res = runBlocking { database.tables("public") }
        logger.info { "数据库表: $res" }
    }

//    @Test
//    fun tableSchemaPg() {
//        val res = runBlocking { database.tableSchema("public.test_01") }
//        logger.info { "数据库表结构: $res" }
//    }

    @Test
    fun tableColumnsPg() {
        val res = runBlocking { database.tableColumns("test_01", "public") }
        logger.info { "数据库字段列表: $res" }
    }

    @Test
    fun tableKeysPg() {
        val res = runBlocking { database.tableKeys("test_01", "public") }
        logger.info { "数据库约束: $res" }
    }

    @Test
    fun tableIndexesPg() {
        val res = runBlocking { database.tableIndexes("test_01", "public") }
        logger.info { "数据库索引: $res" }
    }
}