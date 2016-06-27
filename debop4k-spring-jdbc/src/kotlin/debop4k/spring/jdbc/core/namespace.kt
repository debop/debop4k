/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.spring.jdbc.core

import debop4k.core.functional.Option
import debop4k.core.functional.Option.None
import debop4k.core.functional.Option.Some
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * 결과가 없는 [ResultSet]에 대해 null 을 반환하도록 합니다.
 */
//@Suppress("BASE_WITH_NULLABLE_UPPER_BOUND")
fun <T> emptyResultToNull(body: () -> T): T? = try {
  body()
} catch(e: EmptyResultDataAccessException) {
  null
}

/**
 * 결과 레코드가 없는 [ResultSet]에 대해 [None] 값을 반환합니다.
 */
fun <T> emptyResultToOption(body: () -> T): Option<T> {
  return try {
    Some(body())
  } catch(e: EmptyResultDataAccessException) {
    None
  }
}

fun <T> rowMapperObject(rowMapper: (ResultSet, Int) -> T): RowMapper<T>
    = RowMapper { rs, rowNum -> rowMapper(rs, rowNum) }

inline fun<T> ResultSet.extract(body: ResultSetGetFieldTokens.() -> T): T
    = ResultSetGetFieldTokens(this).body()

inline fun PreparedStatement.arguments(body: PreparedStatementArgumentSetter.() -> Unit): Unit
    = PreparedStatementArgumentSetter(this).body()

fun <T> JdbcOperations.queryEx(sql: String, vararg args: Any, rse: (ResultSet) -> T): T
    = this.query(sql, ResultSetExtractor<T> { rs -> rse(rs) }, *args)

fun <T> JdbcOperations.queryEx(sql: String, vararg args: Any, rowMapper: (ResultSet, Int) -> T): List<T>
    = this.query(sql, rowMapperObject(rowMapper), *args)

fun <T> JdbcOperations.queryExForObject(sql: String, vararg args: Any, rowMapper: (ResultSet, Int) -> T): T
    = this.queryForObject(sql, rowMapperObject(rowMapper), *args)

