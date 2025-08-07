package com.kxxnzstdsw


import com.kxxnzstdsw.impl.PgDatabaseMetaDataImpl
import com.kxxnzstdsw.impl.PgTableMetaDataImpl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class Database(val uri: String) {
    private val driverName: String

    private val databaseMetaData: DatabaseMetaData

    /** catalog -> schema -> dataSource*/
    private val dataSourceMap = mutableMapOf<String, MutableMap<String, HikariDataSource>>()

    /** catalog -> schema -> TableMetaData*/
    private val tableMetaDataMap = mutableMapOf<String, MutableMap<String, TableMetaData>>()

    /** catalog -> schema -> Executor*/
    private val executorMap = mutableMapOf<String, MutableMap<String, Executor>>()


    init {
        val dataSource = HikariDataSource(HikariConfig().apply {
            maximumPoolSize = 2
            jdbcUrl = uri
        })
        driverName = runBlocking(Dispatchers.IO) { dataSource.connection.use { it.metaData.driverName } }
        databaseMetaData = when (driverName) {
            "PostgreSQL JDBC Driver" -> PgDatabaseMetaDataImpl(dataSource)
            else -> throw RuntimeException("不支持的数据库驱动")
        }
    }

    private fun dataSource(catalog: String, schema: String): HikariDataSource {

        val conf = HikariConfig().apply {
            maximumPoolSize = 5
            jdbcUrl = uri
            this.catalog = catalog
            this.schema = schema
        }

        return dataSourceMap.getOrPut(catalog) {
            mutableMapOf()
        }.getOrPut(schema) {
            HikariDataSource(conf)
        }

    }

    private fun tableMetaData(catalog: String, schema: String): TableMetaData {
        return tableMetaDataMap.getOrPut(catalog) {
            mutableMapOf()
        }.getOrPut(schema) {
            when (driverName) {
                "PostgreSQL JDBC Driver" -> PgTableMetaDataImpl(dataSource(catalog, schema))
                else -> throw RuntimeException("不支持的数据库驱动")
            }
        }
    }

    private fun executor(catalog: String, schema: String): Executor {
        return executorMap.getOrPut(catalog) {
            mutableMapOf()
        }.getOrPut(schema) {
            Executor(dataSource(catalog, schema))
        }
    }

    suspend fun databaseVersion() = databaseMetaData.databaseVersion()
    suspend fun databases() = databaseMetaData.databases()
    suspend fun users() = databaseMetaData.users()
    suspend fun roles() = databaseMetaData.roles()
    suspend fun tables(catalog: String, schema: String) =
        tableMetaData(catalog, schema).tables(schema)

    suspend fun tableColumns(tableName: String, catalog: String, schema: String) =
        tableMetaData(catalog, schema).tableColumns(tableName, schema)

    suspend fun tableKeys(tableName: String, catalog: String, schema: String) =
        tableMetaData(catalog, schema).tableKeys(tableName, schema)

    suspend fun tableIndexes(tableName: String, catalog: String, schema: String) =
        tableMetaData(catalog, schema).tableIndexes(tableName, schema)

    suspend fun execute(sql: String, catalog: String, schema: String) = executor(catalog, schema).execute(sql)


}