package com.kxxnzstdsw.app.database.model

data class Key(
    /**架构名*/
    val schema: String,
    /**主键名*/
    val constraintName: String,
    /**列名*/
    val columnName: String,
    /**约束类型*/
    val constraintType: String
)
