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

package debop4k.data.spring.boot.autoconfigure

import debop4k.config.database.DatabaseConfigElement
import debop4k.core.uninitialized
import debop4k.data.DataSources
import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Hikari CP 라이브러리를 이용한 DataSource 설정 정보를 나타내는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@ConfigurationProperties(prefix = HikariDataSourceProperties.PREFIX)
open class HikariDataSourceProperties {

  companion object {
    const val PREFIX = "debop4k.datasource"
  }

  /** Driver class name */
  var driverClassName: String = ""
  /** jdbc url (ex: jdbc:h2:mem:test) */
  var jdbcUrl: String = ""
  /** database user name */
  var username: String? = null
  /** database user password */
  var password: String? = null

  /** Maximum connection pool size */
  var maxPoolSize: Int = DataSources.MAX_POOL_SIZE
  /** Minimum connection idle size */
  var minIdleSize: Int = DataSources.MIN_IDLE_SIZE

  /** Test query */
  var testQuery: String = DatabaseConfigElement.TEST_QUERY

  /** Database migration 을 위한 Flyway 설정 */
  var flyway: Flyway? = uninitialized()
  /** PostgreSQL DB를 위한 추가 설정 */
  var postgres: Postgres? = uninitialized()
  /** MySQL DB를 위한 추가 설정 */
  var mysql: MySql? = uninitialized()
  /** Oracle DB를 위한 추가 설정 */
  var oracle: Oracle? = uninitialized()


  class Flyway {
    /** 작업 전에 Database를 초기화 할 것인가? */
    var isClearDatabase: Boolean = false
    /** Flyway 작업 (기본 = migrate) */
    var initMethod: String = "" //"migrate";

    var isBaselineOnMigrate: Boolean = false
  }

  class Postgres {
    var preparedStatementCacheQueries: Int = 1024
    var preparedStatementCacheSize: Int = 64
    var tcpKeepAlive: Boolean = true
    var charset: String = "UTF-8"

    fun toMap(): MutableMap<String, String> {
      val map = Maps.mutable.of<String, String>()
      map.put("preparedStatementCacheQueries", preparedStatementCacheQueries.toString())
      map.put("preparedStatementCacheSizeMiB", preparedStatementCacheSize.toString())
      map.put("tcpKeepAlive", tcpKeepAlive.toString())
      map.put("charset", charset.toString())

      return map
    }
  }

  class MySql {
    var cachePrepStats: Boolean = true
    var prepStmtCacheSize: Int = 500
    var prepStmtCacheSqlLimit: Int = 4096
    var useServerPrepStmts: Boolean = true

    fun toMap(): MutableMap<String, String> {
      val map = Maps.mutable.of<String, String>()

      map.put("cachePrepStats", cachePrepStats.toString())
      map.put("prepStmtCacheSize", prepStmtCacheSize.toString())
      map.put("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit.toString())
      map.put("useServerPrepStmts", useServerPrepStmts.toString())

      return map
    }
  }

  class Oracle {
    // TODO: 추가 환경설정 정보
  }
}