/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.data.jdbc

import debop4k.core.loggerOf
import lombok.NonNull
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import javax.inject.Inject
import javax.sql.DataSource

/**
 * Jdbc Data Access Object
 *
 * @author sunghyouk.bae@gmail.com
 */
@Component
@Transactional
open class JdbcDao @Inject constructor(val dataSource: DataSource,
                                       val template: NamedParameterJdbcTemplate) {

  private val log = loggerOf(javaClass)

  open fun <T> query(sql: String, mapper: (ResultSet, Int) -> T): List<T>
      = template.query(sql, mapper)

  open fun <T> query(sql: String, mapper: RowMapper<T>): List<T>
      = template.query(sql, mapper)

  open fun <T> query(sql: String, params: SqlParameterSource, mapper: (ResultSet, Int) -> T): List<T>
      = template.query(sql, params, mapper)

  open fun <T> query(sql: String, params: SqlParameterSource, mapper: RowMapper<T>): List<T>
      = template.query(sql, params, mapper)

  open fun update(sql: String, args: Map<String, *>): Int = template.update(sql, args)

  /**
   * 엔티티를 지정한 테이블에 추가합니다.
   *
   * @param entity    저장할 엔티티
   * @param tablename 대상 테이블
   * @return Auto Increment key 값
   */
  open fun addAndReturnKey(entity: Any, tableName: String): Number {
    val params = BeanPropertySqlParameterSource(entity)
    val jdbcInsert = SimpleJdbcInsert(dataSource).withTableName(tableName)
    return jdbcInsert.executeAndReturnKey(params)
  }

  open fun <T> execute(sql: String, params: Map<String, *>): T {
    return template.execute<T>(sql, params, null)
  }

  open fun batchUpdate(sql: String, entities: Collection<*>): IntArray {
    val paramsArray = SqlParameterSourceUtils.createBatch(entities.toTypedArray())
    return template.batchUpdate(sql, paramsArray)
  }

  open fun callProcedure(@NonNull procName: String, params: Map<String, *>): Map<String, Any> {
    val jdbcCall = SimpleJdbcCall(dataSource).withProcedureName(procName)
    return jdbcCall.execute(params)
  }

  open fun callProcedure(procName: String, vararg parameters: SqlParameter): Map<String, Any> {
    val jdbcCall = SimpleJdbcCall(dataSource).withProcedureName(procName).declareParameters(*parameters)
    return jdbcCall.execute()
  }

  @Suppress("UNCHECKED_CAST")
  open fun <T> callProcedure(procName: String,
                             parameterName: String,
                             rowMapper: (ResultSet, Int) -> T,
                             vararg parameters: SqlParameter): List<T> {
    return SimpleJdbcCall(dataSource)
        .withProcedureName(procName)
        .declareParameters(*parameters)
        .returningResultSet(parameterName, rowMapper)
        .execute()
        .get(parameterName) as List<T>
  }

  @Suppress("UNCHECKED_CAST")
  open fun <T> callProcedure(procName: String,
                             parameterName: String,
                             rowMapper: RowMapper<T>,
                             vararg parameters: SqlParameter): List<T> {
    return SimpleJdbcCall(dataSource)
        .withProcedureName(procName)
        .declareParameters(*parameters)
        .returningResultSet(parameterName, rowMapper)
        .execute()
        .get(parameterName) as List<T>
  }

  open fun <T> callFunction(functionName: String,
                            returnType: Class<T>,
                            vararg parameters: SqlParameter): T {
    return SimpleJdbcCall(dataSource)
        .withFunctionName(functionName)
        .declareParameters(*parameters)
        .withReturnValue()
        .executeFunction(returnType)
  }
}