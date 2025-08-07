package com.kxxnzstdsw.app.database.model

data class Columns(
    val columnName: String,
    val dataType: String,
    val characterMaximumLength: Int?,
    val isNullable: String,
    val columnDefault: String?
)
