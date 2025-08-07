package com.kxxnzstdsw.model

data class Column(
    /**架构名*/
    val schema: String,
    /**列名*/
    val columnName: String,
    /**数据类型*/
    val dataType: String,
    /**字符最大长度*/
    val characterMaximumLength: Int?,
    /**是否可空*/
    val isNullable: String,
    /**默认值*/
    val columnDefault: String?,
    val ordinalPosition: Int
)
