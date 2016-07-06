/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.data.factory

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import debop4k.data.DataSources.MAX_POOL_SIZE
import debop4k.data.DataSources.MIN_IDLE_SIZE
import debop4k.data.DatabaseSetting
import debop4k.data.JdbcDrivers.DRIVER_CLASS_MARIADB
import debop4k.data.JdbcDrivers.DRIVER_CLASS_MYSQL
import debop4k.data.JdbcDrivers.DRIVER_CLASS_POSTGRESQL
import org.apache.commons.lang3.builder.ToStringBuilder
import org.slf4j.LoggerFactory
import javax.sql.DataSource

/**
 * HikariCP 라이브러리의 {@link HikariDataSource} 를 생성해주는 Factory 입니다.
 * <p>
 * 참고 : https://brettwooldridge.github.io/HikariCP/
 *
 * @author sunghyouk.bae@gmail.com
 */
class HikariDataSourceFactory : DataSourceFactory {

  private val log = LoggerFactory.getLogger(javaClass)

  /**
   * [HikariDataSource] 를 생성합니다.
   */
  override fun create(setting: DatabaseSetting): DataSource {
    log.info("Hikari DataSource를 빌드합니다. {}", setting)

    val config = HikariConfig().apply {
      isInitializationFailFast = true
      driverClassName = setting.driverClassName
      jdbcUrl = setting.jdbcUrl
      username = setting.username
      password = setting.password
    }

    val props = setting.props
    if (props != null && !props.isEmpty()) {

      for (key in props) {
        if (key != null && props.get(key) != null) {
          config.addDataSourceProperty(key, props.get(key))
        }
      }
      val isMySQL = DRIVER_CLASS_MYSQL.equals(setting.driverClassName) || DRIVER_CLASS_MARIADB.equals(setting.driverClassName)
      if (isMySQL) {
        config.setMySqlProperty(setting)
      }

      val isPostgreSQL = DRIVER_CLASS_POSTGRESQL.equals(setting.driverClassName)
      if (isPostgreSQL) {
        config.setPostgresProperty(setting)
      }
    }

    // NOTE: HikariCP 2.4.3 이상에서만 지원하는데, 이놈이랑 PostgreSQL Driver 가 궁합이 안맞는다. ㅠ.ㅠ
    config.apply {
      connectionTestQuery = if (setting.testQuery.isNullOrBlank()) "SELECT 1" else setting.testQuery

      maximumPoolSize = Math.max(setting.maxPoolSize, MAX_POOL_SIZE)
      minimumIdle = Math.max(setting.minIdleSize, MIN_IDLE_SIZE)

      // Timeout 설정
      maxLifetime = 600000 // 10 minutes
      idleTimeout = 300000 // 5 minutes
    }

    log.info("Hikari DataSource 를 생성했습니다. config={}", ToStringBuilder.reflectionToString(config))

    return HikariDataSource(config)
  }

  private fun HikariConfig.setMySqlProperty(setting: DatabaseSetting): HikariConfig {
    log.debug("MySQL용 추가 설정을 수행합니다.")
    addDataSourceProperty("cachePrepStmts", setting.props?.getIfAbsentValue("cachePrepStmts", "true"))
    addDataSourceProperty("prepStmtCacheSize", setting.props?.getIfAbsentValue("prepStmtCacheSize", "500"))
    addDataSourceProperty("prepStmtCacheSqlLimit", setting.props?.getIfAbsentValue("prepStmtCacheSqlLimit", "4096"))
    addDataSourceProperty("useServerPrepStmts", setting.props?.getIfAbsentValue("useServerPrepStmts", "true"))
    return this
  }

  private fun HikariConfig.setPostgresProperty(setting: DatabaseSetting): HikariConfig {
    log.debug("PostgreSQL용 추가 설정을 수행합니다.")
    addDataSourceProperty("preparedStatementCacheQueries", setting.props?.getIfAbsentValue("preparedStatementCacheQueries", "1024"))
    addDataSourceProperty("preparedStatementCacheSizeMiB", setting.props?.getIfAbsentValue("preparedStatementCacheSizeMiB", "64"))
    addDataSourceProperty("tcpKeepAlive", setting.props?.getIfAbsentValue("tcpKeepAlive", "true"))
    addDataSourceProperty("charset", setting.props?.getIfAbsentValue("charset", "UTF-8"))
    return this
  }
}