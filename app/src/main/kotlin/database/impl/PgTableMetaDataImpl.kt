package com.kxxnzstdsw.app.database.impl

import com.kxxnzstdsw.app.database.TableMetaData
import com.kxxnzstdsw.app.database.model.Column
import com.kxxnzstdsw.app.database.model.Index
import com.kxxnzstdsw.app.database.model.Key
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers

class PgTableMetaDataImpl(val dataSource: HikariDataSource, val databaseName: String = "postgres") : TableMetaData {
    private val logger = KotlinLogging.logger {}

    /**全部表*/
    override suspend fun tables(schema: String) = with(Dispatchers.IO) {
        dataSource.apply {
            catalog = databaseName
        }.connection.use { connection ->
            connection.prepareStatement("SELECT tablename FROM pg_tables WHERE schemaname = ?").use { statement ->
                statement.setString(1, schema)
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

    override suspend fun tableColumns(tableName: String, schema: String): List<Column> = with(Dispatchers.IO) {
        dataSource.apply {
            catalog = databaseName
        }.connection.use { connection ->
            connection.prepareStatement(
                """
                SELECT  table_schema,
                        column_name,              -- 列名
                        data_type,                -- 数据类型
                        character_maximum_length, -- 字符类型的最大长度（如 varchar(50) 则为 50）
                        is_nullable,              -- 是否允许为空（YES/NO）
                        column_default,           -- 默认值
                        ordinal_position          -- 列的顺序
                FROM information_schema.columns
                WHERE table_name = ? -- 替换为目标表名
                  AND table_schema = ?;
            """.trimIndent()
            ).use { statement ->
                return statement.apply {
                    setString(1, tableName)
                    setString(2, schema)
                }.executeQuery().use { resultSet ->
                    val columns = mutableListOf<Column>()
                    while (resultSet.next()) {
                        columns.add(
                            Column(
                                schema = resultSet.getString(1),
                                columnName = resultSet.getString(2),
                                dataType = resultSet.getString(3),
                                characterMaximumLength = resultSet.getInt(4),
                                isNullable = resultSet.getString(5),
                                columnDefault = resultSet.getString(6),
                                ordinalPosition = resultSet.getInt(7),
                            )
                        )
                    }
                    columns.apply { sortBy { it.ordinalPosition } }
                }
            }
        }
    }

    override suspend fun tableKeys(tableName: String, schema: String): List<Key> = with(Dispatchers.IO) {
        dataSource.apply {
            catalog = databaseName
        }.connection.use { connection ->
            connection.prepareStatement(
                """
                SELECT tc.table_schema,
                       tc.constraint_name,
                       kcu.column_name,
                       tc.constraint_type
                FROM information_schema.table_constraints tc
                         JOIN information_schema.key_column_usage kcu
                              ON tc.constraint_name = kcu.constraint_name
                                  and tc.table_schema = kcu.table_schema
                WHERE tc.table_name = ? and tc.table_schema = ?;
            """.trimIndent()
            )
                .use { statement ->
                    statement.apply {
                        setString(1, tableName)
                        setString(2, schema)
                    }.executeQuery().use { resultSet ->
                        val keys = mutableListOf<Key>()
                        while (resultSet.next()) {
                            keys.add(
                                Key(
                                    schema = resultSet.getString(1),
                                    constraintName = resultSet.getString(2),
                                    columnName = resultSet.getString(3),
                                    constraintType = resultSet.getString(4),
                                )
                            )
                        }
                        keys
                    }
                }
        }
    }

    override suspend fun tableIndexes(tableName: String, schema: String): List<Index> = with(Dispatchers.IO) {
        dataSource.apply {
            catalog = databaseName
        }.connection.use { connection ->
            connection.prepareStatement(
                """
                SELECT schemaname, tablename, indexname, indexdef
                FROM pg_indexes
                WHERE tablename = ? and schemaname = ?;
            """.trimIndent()
            ).use { statement ->
                statement.apply {
                    setString(1, tableName)
                    setString(2, schema)
                }.executeQuery().use { resultSet ->
                    val indexes = mutableListOf<Index>()
                    while (resultSet.next()) {
                        indexes.add(
                            Index(
                                schema = resultSet.getString(1),
                                tableName = resultSet.getString(2),
                                indexName = resultSet.getString(3),
                                indexDef = resultSet.getString(4),
                            )
                        )
                    }
                    indexes
                }
            }
        }
    }
}