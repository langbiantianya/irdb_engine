package com.kxxnzstdsw.impl

import com.kxxnzstdsw.DatabaseMetaData
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers

class PgDatabaseMetaDataImpl(val dataSource: HikariDataSource) : DatabaseMetaData {
    override suspend fun databaseVersion(): String? = with(Dispatchers.IO) {
        //        获取数据库相关信息例如版本、操作系信息
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT version()").use { resultSet ->
                    return if (resultSet.next()) {
                        resultSet.getString(1)
                    } else {
                        null
                    }
                }
            }
        }
    }


    override suspend fun roles() = with(Dispatchers.IO) {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT rolname FROM pg_roles").use { resultSet ->
                    val roles = mutableListOf<String>()
                    while (resultSet.next()) {
                        roles.add(resultSet.getString(1))
                    }
                    roles
                }
            }
        }
    }


    override suspend fun users() = with(Dispatchers.IO) {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT usename FROM pg_user").use { resultSet ->
                    val users = mutableListOf<String>()
                    while (resultSet.next()) {
                        users.add(resultSet.getString(1))
                    }
                    users
                }
            }
        }
    }


    override suspend fun databases() = with(Dispatchers.IO) {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT datname FROM pg_database WHERE datistemplate = false").use { resultSet ->
                    val databases = mutableListOf<String>()
                    while (resultSet.next()) {
                        databases.add(resultSet.getString(1))
                    }
                    databases
                }
            }
        }
    }

}