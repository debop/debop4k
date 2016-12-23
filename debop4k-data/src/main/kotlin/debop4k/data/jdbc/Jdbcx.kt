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

@file:JvmName("Jdbcx")

package debop4k.data.jdbc

import debop4k.core.loggerOf
import org.slf4j.Logger
import java.sql.*
import javax.sql.DataSource

private val log: Logger = loggerOf("Jdbcx")

/**
 * [DataSource]의 [Connection] 이용하여 지정한 [block] 을 실행합니다.
 */
inline fun <T> DataSource.use(crossinline block: (Connection) -> T): T {
  val conn = this.connection

  conn?.let { c ->
    c.use { cc ->
      return block(cc)
    }
  } ?: error("No Connection returns from $this")
}

inline fun <T> DataSource.statement(crossinline block: (Statement) -> T): T
    = this.use { it.statement(block) }

fun <T> DataSource.update(sql: String): Int
    = this.use { it.update(sql) }

inline fun <T> DataSource.query(sql: String, crossinline block: (ResultSet) -> T): T
    = this.use { it.query(sql, block) }

/**
 * 지정한 [jdbcUrl] 을 이용하여 [Connection] 을 생성합니다.
 */
fun connectionOf(jdbcUrl: String, info: Map<String, String>): Connection
    = DriverManager.getConnection(jdbcUrl, info.toProperties())

/**
 * 지정한 [jdbcUrl] 을 이용하여 [Connection] 을 생성합니다.
 */
@JvmOverloads
fun connectionOf(jdbcUrl: String, username: String? = null, password: String? = null): Connection
    = DriverManager.getConnection(jdbcUrl, username, password)

inline fun <T> Connection.use(block: (Connection) -> T): T {
  try {
    return block(this)
  } catch(e: Throwable) {
    throw SQLDataException(e)
  } finally {
    this.close()
  }
}

/**
 * [Connection]에 해당하는 DB에 [block] 을 실행합니다.
 */
inline fun <T> Connection.statement(crossinline block: (Statement) -> T): T {
  val statement = this.createStatement()
  return statement?.use(block) ?: error("No Statement")
}

/**
 * [Connection]에 해당하는 DB에 [sql] 구문을 실행합니다.
 */
fun Connection.update(sql: String): Int {
  return if (sql.isBlank()) {
    return -1
  } else {
    statement { it.executeUpdate(sql) }
  }
}

/**
 * [Connection]에 해당하는 DB에 [sql] 구문을 실행하고, 결과를 [mapper] 을 이용하여 객체로 빌드합니다.
 */
inline fun <T> Connection.query(sql: String, crossinline mapper: (ResultSet) -> T): T {
  return statement {
    it.executeQuery(sql).use(mapper)
  }
}


inline fun <T, S : Statement> S.use(mapper: (S) -> T): T {
  try {
    return mapper(this)
  } finally {
    this.close()
  }
}

fun PreparedStatement.update(): Int {
  try {
    return this.executeUpdate()
  } finally {
    this.close()
  }
}

inline fun <T> PreparedStatement.query(mapper: (ResultSet) -> T): T {
  try {
    val rs = this.executeQuery()
    return mapper(rs)
  } finally {
    this.close()
  }
}

inline fun <T> ResultSet.use(mapper: (ResultSet) -> T): T {
  try {
    return mapper(this)
  } finally {
    this.close()
  }
}

operator fun ResultSet.iterator(): Iterator<ResultSet> {
  val rs = this
  return object : Iterator<ResultSet> {
    override fun hasNext(): Boolean = rs.next()
    override fun next(): ResultSet = rs
  }
}

inline fun <T> ResultSet.iterator(crossinline mapper: (ResultSet) -> T): Iterator<T> {
  val rs = this
  return object : Iterator<T> {
    override fun hasNext(): Boolean = rs.next()
    override fun next(): T = mapper(rs)
  }
}

inline fun <T> ResultSet.map(crossinline mapper: (ResultSet) -> T): Iterable<T> {
  val rs = this
  return object : Iterable<T> {
    override fun iterator(): Iterator<T> = rs.iterator(mapper)
  }
}

val ResultSet.columnNames: Array<String>
  get() {
    val meta = this.metaData
    return Array(meta.columnCount, { meta.getColumnName(it + 1) ?: it.toString() })
  }

operator fun ResultSet.get(columnId: Int): Any?
    = this.getObject(columnId)

private fun ResultSet.ensureHasRow(): ResultSet {
  if (!this.next())
    error("There are no rows left in cursor")
  return this
}

fun ResultSet.singleInt(): Int = this.ensureHasRow().getInt(1)

fun ResultSet.singleLong(): Long = this.ensureHasRow().getLong(1)

fun ResultSet.singleDouble(): Double = this.ensureHasRow().getDouble(1)



