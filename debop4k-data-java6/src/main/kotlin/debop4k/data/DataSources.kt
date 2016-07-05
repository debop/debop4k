/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import debop4k.core.min
import debop4k.data.factory.HikariDataSourceFactory
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import javax.sql.DataSource

/**
 * @author debop sunghyouk.bae@gmail.com
 */
object DataSources {

  private val processCount = Runtime.getRuntime().availableProcessors()
  val MAX_POOL_SIZE: Int = processCount * 16
  val MIN_POOL_SIZE: Int = processCount
  val MIN_IDLE_SIZE: Int = 2 min processCount

  private val dataSourceCache = ConcurrentHashMap<DatabaseSetting, DataSource>()

  private val dataSourceFactory = HikariDataSourceFactory()

  fun of(driverClassName: String,
         jdbcUrl: String,
         username: String? = null,
         password: String? = null): DataSource {

    val config = HikariConfig()

    return HikariDataSource(config)
  }

  fun of(setting: DatabaseSetting): DataSource {
    return dataSourceCache.getIfAbsentPut(setting, dataSourceFactory.create(setting))
  }

  fun ofEmbeddedH2() = of(h2Mem)
  fun ofEmbeddedHsql() = of(hsqlMem)

  val h2Mem = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_H2,
                              jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE;")

  val hsqlMem = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_HSQL,
                                jdbcUrl = "jdbc:hsqldb:mem:test",
                                username = "sa",
                                testQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
}