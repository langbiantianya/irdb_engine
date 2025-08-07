package com.kxxnzstdsw

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.*
import java.sql.ResultSet
import java.sql.Types
import java.time.OffsetDateTime
import java.time.OffsetTime

class Executor(val dataSource: HikariDataSource) {

    /**
     * 执行查询并动态包装结果为JSON
     * @param sql SQL查询语句
     * @param params SQL参数（可选）
     * @return 序列化后的JSON字符串
     */
    suspend fun execute(sql: String, params: List<Any?> = emptyList()): JsonArray =
        with(Dispatchers.IO) {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    // 设置SQL参数
                    params.forEachIndexed { index, value ->
                        stmt.setObject(index + 1, value) // JDBC参数索引从1开始
                    }
                    // 执行查询
                    stmt.executeQuery().use { rs ->
                        resultSetToList(rs)
                    }
                }
            }
        }

    /**
     * 将ResultSet转换为List<Map<String, Any?>>
     */
    private fun resultSetToList(rs: ResultSet): JsonArray {
        val resultList = mutableListOf<JsonObject>()

        val metaData = rs.metaData
        val columnCount = metaData.columnCount

        // 遍历每行数据
        while (rs.next()) {
            val rowMap = mutableMapOf<String, JsonElement>()
            // 遍历每列数据（列索引从1开始）
            for (i in 1..columnCount) {
                val columnName = metaData.getColumnName(i) // 获取列名
                val columnType = metaData.getColumnType(i)

                // 数据库类型与Java类型映射
                rowMap[columnName] = when (columnType) {
                    // 数值类型
                    Types.TINYINT -> JsonPrimitive(rs.getByte(i))
                    Types.SMALLINT -> JsonPrimitive(rs.getShort(i))
                    Types.INTEGER -> JsonPrimitive(rs.getInt(i))
                    Types.BIGINT -> JsonPrimitive(rs.getLong(i))
                    Types.FLOAT -> JsonPrimitive(rs.getFloat(i))
                    Types.REAL -> JsonPrimitive(rs.getFloat(i))
                    Types.DOUBLE -> JsonPrimitive(rs.getDouble(i))
                    Types.NUMERIC -> JsonPrimitive(rs.getBigDecimal(i))
                    Types.DECIMAL -> JsonPrimitive(rs.getBigDecimal(i))

                    // 字符串类型
                    Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR, Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR -> JsonPrimitive(
                        rs.getString(
                            i
                        )
                    )
                    // 二进制类型
                    Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> JsonPrimitive(rs.getBytes(i)?.toBase64())
                    Types.BLOB -> JsonPrimitive(rs.getByte(i))

                    // 日期时间类型
                    Types.DATE -> JsonPrimitive(rs.getDate(i)?.toString())
                    Types.TIME -> JsonPrimitive(rs.getTime(i)?.toString())
                    Types.TIMESTAMP -> JsonPrimitive(rs.getTimestamp(i)?.toString())
                    Types.TIME_WITH_TIMEZONE -> JsonPrimitive(rs.getObject(i, OffsetTime::class.java)?.toString())
                    Types.TIMESTAMP_WITH_TIMEZONE -> JsonPrimitive(
                        rs.getObject(i, OffsetDateTime::class.java)?.toString()
                    )

                    // 其他类型
                    Types.BOOLEAN -> JsonPrimitive(rs.getBoolean(i))
                    Types.ARRAY -> resultSetToList(rs.getArray(i).resultSet)
                    Types.CLOB -> JsonPrimitive(rs.getBytes(i)?.toBase64())
                    Types.NCLOB -> JsonPrimitive(rs.getBytes(i)?.toBase64())
                    Types.SQLXML -> JsonPrimitive(rs.getSQLXML(i)?.toString())
                    Types.DATALINK -> JsonPrimitive(rs.getString(i))
                    Types.OTHER -> JsonPrimitive(rs.getObject(i)?.toString())
                    Types.NULL -> JsonNull
                    // 未知类型
                    else -> JsonPrimitive(rs.getObject(i)?.toString())
                }

            }
            resultList.add(JsonObject(rowMap))
        }
        return JsonArray(resultList)
    }
}

