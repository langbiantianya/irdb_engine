package com.kxxnzstdsw.app.database

interface DatabaseMetaData {
    /**数据库版本*/
    suspend fun databaseVersion(): String?

    /**数据库角色*/
    suspend fun roles(): List<String>

    /**数据库用户*/
    suspend fun users(): List<String>

    /**全部数据库*/
    suspend fun databases(): List<String>

}