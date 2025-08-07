package com.kxxnzstdsw.app.database

import com.kxxnzstdsw.app.database.model.Column
import com.kxxnzstdsw.app.database.model.Index
import com.kxxnzstdsw.app.database.model.Key

interface TableMetaData {
    /**全部表*/
    suspend fun tables(schema: String): List<String>

    /**表结构*/
    suspend fun tableColumns(tableName: String, schema: String): List<Column>

    /**主键*/
    suspend fun tableKeys(tableName: String, schema: String): List<Key>

    /**索引*/
    suspend fun tableIndexes(tableName: String, schema: String): List<Index>
}