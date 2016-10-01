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

package debop4k.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import debop4k.config.database.DatabaseConfigElement
import debop4k.config.database.DatabaseSetting
import debop4k.core.loggerOf
import debop4k.core.utils.min
import debop4k.data.factory.HikariDataSourceFactory
import debop4k.data.spring.boot.autoconfigure.HikariDataSourceProperties
import org.apache.commons.lang3.builder.ToStringBuilder
import org.eclipse.collections.impl.factory.Maps
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.slf4j.Logger
import javax.sql.DataSource

/**
 * [DataSource] 를 생성해주는 Object
 *
 * @author sunghyouk.bae@gmail.com
 */
object DataSources {

  private val log: Logger = loggerOf(DataSources::class.java)

  @JvmField val PROCESS_COUNT: Int = Runtime.getRuntime().availableProcessors()

  @JvmField val MAX_POOL_SIZE: Int = PROCESS_COUNT * 8
  @JvmField val MIN_POOL_SIZE: Int = PROCESS_COUNT
  @JvmField val MIN_IDLE_SIZE: Int = PROCESS_COUNT min 2

  /** 생성된 DataSource 를 캐시합니다 */
  private val dataSourceCache = ConcurrentHashMap<DatabaseSetting, DataSource>()

  private val dataSourceFactory by lazy { HikariDataSourceFactory() }

  /**
   * Database 접속 설정 정보를 이용하여 DataSource를 생성합니다.
   * @param setting Database 설정 정보 {@link DatabaseSetting}
   * @return DataSource instance.
   */
  @JvmStatic fun of(setting: DatabaseSetting): DataSource {
    return dataSourceFactory.create(setting)
//    return dataSourceCache.getIfAbsentPut(setting, dataSourceFactory.create(setting))
  }

  /**
   * Database 접속 설정 정보를 이용하여 DataSource 를 생성합니다.
   * @param dataSourceProps spring boot 용 환경설정 정보
   * @return DataSource instance.
   */
  @JvmStatic fun of(dataSourceProps: HikariDataSourceProperties): DataSource
      = of(convertToDatabaseSetting(dataSourceProps))

  /**
   * [HikariConfig] 를 이용하여 DataSource를 생성합니다.

   * @param config [HikariConfig] 인스턴스
   * *
   * @return [DataSource] 인스턴스
   */
  @JvmStatic fun withConfig(config: HikariConfig): DataSource {
    log.info("Hikari DataSource를 빌드합니다. config={}", ToStringBuilder.reflectionToString(config))
    return HikariDataSource(config)
  }

  /**
   * HSQL DB 용 DataSource 를 생성합니다.
   * NOTE: HSQL는 Multi Thread 에서 In-Memory DB를 지원하지 않습니다.
   * @return HSQL DB 용 DataSource
   */
  @JvmStatic fun ofEmbeddedHSql(): DataSource {
    return of(hsqlMem)
  }

  /**
   * H2 Database 용 DataSource 를 생성합니다.
   * @return DataSource for H2 Database
   */
  @JvmStatic fun ofEmbeddedH2(): DataSource {
    return of(h2Mem)
  }

  /**
   * Memory HSQL DB 용 Database Setting 정보
   */
  @JvmField val hsqlMem = DatabaseSetting("",
                                          "",
                                          JdbcDrivers.DRIVER_CLASS_HSQL,
                                          "jdbc:hsqldb:mem:test",
                                          "sa",
                                          "",
                                          MAX_POOL_SIZE,
                                          MIN_IDLE_SIZE,
                                          "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS",
                                          Maps.mutable.of<String, String>())

  /**
   * Memory H2 DB 용 Database Setting 정보
   */
  @JvmField val h2Mem = DatabaseSetting("",
                                        "",
                                        JdbcDrivers.DRIVER_CLASS_H2,
                                        "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE;",
                                        "sa",
                                        "",
                                        MAX_POOL_SIZE,
                                        MIN_IDLE_SIZE,
                                        DatabaseConfigElement.TEST_QUERY,
                                        Maps.mutable.of<String, String>())

  @JvmStatic
  private fun convertToDatabaseSetting(dataSourceProps: HikariDataSourceProperties): DatabaseSetting {
    log.debug("props={}", dataSourceProps)
    val setting = DatabaseSetting("",
                                  "",
                                  dataSourceProps.driverClassName ?: "",
                                  dataSourceProps.jdbcUrl ?: "",
                                  dataSourceProps.username,
                                  dataSourceProps.password,
                                  dataSourceProps.maxPoolSize,
                                  dataSourceProps.minIdleSize,
                                  dataSourceProps.testQuery ?: DatabaseConfigElement.TEST_QUERY,
                                  Maps.mutable.of<String, String>())

    dataSourceProps.postgres?.let {
      setting.props.putAll(it.toMap())
    }

    dataSourceProps.mysql?.let {
      setting.props.putAll(it.toMap())
    }

    return setting
  }
}