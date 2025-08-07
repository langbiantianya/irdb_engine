package com.kxxnzstdsw.app.database

import com.kxxnzstdsw.app.database.model.Columns

interface TableMetaData {
    /**全部表*/
    suspend fun tables(schemaName: String = "public"): List<String>

    /**DDL语句*/
    suspend fun tableSchema(tableName: String): String?

    /**获取表结构*/
    suspend fun tableStructure(tableName: String): Columns?
}