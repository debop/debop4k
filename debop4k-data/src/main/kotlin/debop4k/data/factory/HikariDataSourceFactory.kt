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

package debop4k.data.factory

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import debop4k.config.database.DatabaseConfigElement
import debop4k.config.database.DatabaseSetting
import debop4k.core.loggerOf
import debop4k.core.utils.max
import debop4k.data.DataSources
import debop4k.data.JdbcDrivers
import org.slf4j.Logger
import javax.sql.DataSource

/**
 * HikariCP 라이브러리의 {@link HikariDataSource} 를 생성해주는 Factory 입니다.
 * <p>
 * 참고 : https://brettwooldridge.github.io/HikariCP/
 *
 * @author sunghyouk.bae@gmail.com
 */
class HikariDataSourceFactory : DataSourceFactory {

  private val log: Logger = loggerOf(javaClass)

  /**
   * [DataSource] 를 생성합니다.
   * @param setting 데이터베이스 연결 정보
   * @return 생성된 [DataSource] 인스턴스. 실패 시 null 반환
   */
  override fun create(setting: DatabaseSetting): DataSource {
    log.info("Hikari DataSource를 빌드합니다... setting={}", setting)

    val config = HikariConfig().apply {
      isInitializationFailFast = true
      driverClassName = setting.driverClass
      jdbcUrl = setting.jdbcUrl
      username = setting.username
      password = setting.password

      maximumPoolSize = setting.maxPoolSize max DataSources.MAX_POOL_SIZE
      minimumIdle = setting.minIdleSize max DataSources.MIN_IDLE_SIZE
      maxLifetime = 600000  // 10 minutes
      idleTimeout = 300000  // 5 minutes

      // NOTE: HikariCP 2.4.3 이상에서만 지원하는데, 이놈이랑 PostgreSQL Driver 가 궁합이 안맞는다. ㅠ.ㅠ
      if (setting.testQuery.isNotBlank()) {
        connectionTestQuery = setting.testQuery
      } else {
        connectionTestQuery = DatabaseConfigElement.TEST_QUERY;
      }
    }

    val props = setting.props
    if (props.isNotEmpty()) {
      props.forEachKeyValue { k, v ->
        if (k != null && v != null)
          config.addDataSourceProperty(k, v)
      }
      setVendorProperties(config, setting)
    }

    log.info("Hikari DataSource를 생성했습니다. conofig={}", config)

    return HikariDataSource(config)
  }

  fun setVendorProperties(config: HikariConfig, setting: DatabaseSetting): Unit {
    val isMySQL = JdbcDrivers.isMySQL(setting.driverClass)
    if (isMySQL) {
      setMySQLProperties(config, setting)
    }

    val isPostgreSQL = JdbcDrivers.isPostgreSQL(setting.driverClass)
    if (isPostgreSQL) {
      setPostgreSQLProperties(config, setting)
    }
  }

  fun setMySQLProperties(config: HikariConfig, setting: DatabaseSetting): Unit {
    with(setting.props) {
      config.addDataSourceProperty("cachePrepStmts", getIfAbsentValue("cachePrepStmts", "true"))
      config.addDataSourceProperty("prepStmtCacheSize", getIfAbsentValue("prepStmtCacheSize", "500"))
      config.addDataSourceProperty("prepStmtCacheSqlLimit", getIfAbsentValue("prepStmtCacheSqlLimit", "4096"))
      config.addDataSourceProperty("useServerPrepStmts", getIfAbsentValue("useServerPrepStmts", "true"))
    }
  }

  fun setPostgreSQLProperties(config: HikariConfig, setting: DatabaseSetting): Unit {
    with(setting.props) {
      config.addDataSourceProperty("preparedStatementCacheQueries", getIfAbsentValue("preparedStatementCacheQueries", "1024"))
      config.addDataSourceProperty("preparedStatementCacheSizeMiB", getIfAbsentValue("preparedStatementCacheSizeMiB", "64"))
      config.addDataSourceProperty("tcpKeepAlive", getIfAbsentValue("tcpKeepAlive", "true"))
      config.addDataSourceProperty("charset", getIfAbsentValue("charset", "UTF-8"))
    }
  }
}