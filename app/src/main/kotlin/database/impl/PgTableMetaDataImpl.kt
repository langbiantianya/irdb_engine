package com.kxxnzstdsw.app.database.impl

import com.kxxnzstdsw.app.database.TableMetaData
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers

class PgTableMetaDataImpl(val dataSource: HikariDataSource, val databaseName: String = "postgres") : TableMetaData {
    /**全部表*/
    override suspend fun tables(schemaName: String) = with(Dispatchers.IO) {
        dataSource.apply {
            catalog = databaseName
        }.connection.use { connection ->
            connection.prepareStatement("SELECT tablename FROM pg_tables WHERE schemaname = ?").use { statement ->
                statement.setString(1, schemaName)
                statement.executeQuery().use { resultSet ->
                    val tables = mutableListOf<String>()
                    while (resultSet.next()) {
                        tables.add(resultSet.getString(1))
                    }
                    tables
                }

            }
        }
    }

    /**DDL语句*/
    override suspend fun tableSchema(tableName: String): String {
        TODO("Not yet implemented")
    }
}