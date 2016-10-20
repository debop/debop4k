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

@file:JvmName("SpringJdbcTemplatex")

package debop4k.data.jdbc

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import java.sql.ResultSet


fun <T> JdbcOperations.query(sql: String, resultsetExtractor: (ResultSet) -> T): T
    = this.query(sql, resultsetExtractor)

fun <T> JdbcOperations.queryList(sql: String, rowMapper: (ResultSet, Int) -> T): List<T>
    = this.query(sql, rowMapper)

fun <T> JdbcOperations.queryObject(sql: String, rowMapper: (ResultSet, Int) -> T): T
    = this.queryForObject(sql, rowMapper)


fun NamedParameterJdbcTemplate.batchUpdate(sql: String, entities: Collection<*>): IntArray {
  val paramsArray = SqlParameterSourceUtils.createBatch(entities.toTypedArray())
  return this.batchUpdate(sql, paramsArray)
}

fun NamedParameterJdbcTemplate.batchUpdate(sql: String, vararg entities: Any?): IntArray {
  val paramsArray = SqlParameterSourceUtils.createBatch(entities)
  return this.batchUpdate(sql, paramsArray)
}

fun NamedParameterJdbcTemplate.batchUpdate(sql: String, vararg valueMaps: Map<String, *>): IntArray {
  val paramsArray = SqlParameterSourceUtils.createBatch(valueMaps)
  return this.batchUpdate(sql, paramsArray)
}