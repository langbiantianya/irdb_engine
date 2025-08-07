package com.kxxnzstdsw.app.database


import com.kxxnzstdsw.app.database.impl.PgDatabaseMetaDataImpl
import com.kxxnzstdsw.app.database.impl.PgTableMetaDataImpl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class Database(val uri: String) {
    private val logger = KotlinLogging.logger {}
    private val dataSource: HikariDataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = uri
    })
    private val driverName = runBlocking(Dispatchers.IO) { dataSource.connection.use { it.metaData.driverName } }


    private val databaseMetaData: DatabaseMetaData = when (driverName) {
        "PostgreSQL JDBC Driver" -> PgDatabaseMetaDataImpl(dataSource)
        else -> throw RuntimeException("不支持的数据库驱动")
    }

    private var defaultCatalog: String? = runBlocking {
        databaseMetaData.databases().firstOrNull()
    }

    private val tableMetaData: TableMetaData = when (driverName) {

        "PostgreSQL JDBC Driver" -> defaultCatalog?.let {
            PgTableMetaDataImpl(dataSource, it)
        }
            ?: PgTableMetaDataImpl(dataSource)

        else -> throw RuntimeException("不支持的数据库驱动")
    }


    suspend fun databaseVersion() = databaseMetaData.databaseVersion()
    suspend fun databases() = databaseMetaData.databases()
    suspend fun users() = databaseMetaData.users()
    suspend fun roles() = databaseMetaData.roles()
    suspend fun tables(schema: String) = tableMetaData.tables(schema)
    suspend fun tableColumns(tableName: String, schema: String) = tableMetaData.tableColumns(tableName, schema)
    suspend fun tableKeys(tableName: String, schema: String) = tableMetaData.tableKeys(tableName, schema)
    suspend fun tableIndexes(tableName: String, schema: String) = tableMetaData.tableIndexes(tableName, schema)


}