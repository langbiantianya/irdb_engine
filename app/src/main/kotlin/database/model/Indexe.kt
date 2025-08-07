package com.kxxnzstdsw.app.database.model

data class Index(
    /**架构名*/
    val schema: String,
    /**表名*/
    val tableName: String,
    /**索引名*/
    val indexName: String,
    /**索引定义*/
    val indexDef: String
)
