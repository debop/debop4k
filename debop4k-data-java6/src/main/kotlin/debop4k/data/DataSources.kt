/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

@file:JvmName("DataSources")

package debop4k.data

import debop4k.core.min
import debop4k.data.factory.HikariDataSourceFactory
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import javax.sql.DataSource

//
// TODO: Extension Functions 로 바꾸자
//

/**
 * [DataSource]를 생성해주는 클래스입니다.
 *
 * @author debop sunghyouk.bae@gmail.com
 */
object DataSources {

  private val processCount by lazy { Runtime.getRuntime().availableProcessors() }
  val MAX_POOL_SIZE: Int by lazy { processCount * 16 }
  val MIN_POOL_SIZE: Int by lazy { processCount }
  val MIN_IDLE_SIZE: Int by lazy { 2 min processCount }

  private val dataSourceCache: ConcurrentHashMap<DatabaseSetting, DataSource>
      = ConcurrentHashMap<DatabaseSetting, DataSource>()

  private val dataSourceFactory = HikariDataSourceFactory()

  @JvmOverloads
  @JvmStatic
  fun of(driverClassName: String,
         jdbcUrl: String,
         username: String? = null,
         password: String? = null): DataSource {

    val setting = DatabaseSetting(driverClassName = driverClassName,
                                  jdbcUrl = jdbcUrl,
                                  username = username,
                                  password = password)
    return of(setting)
  }

  @JvmStatic
  fun of(setting: DatabaseSetting): DataSource {
    return dataSourceCache.getIfAbsentPut(setting, dataSourceFactory.create(setting))
  }

  @JvmStatic fun ofEmbeddedH2(): DataSource = of(h2Mem)
  @JvmStatic fun ofEmbeddedHsql(): DataSource = of(hsqlMem)

  @JvmStatic val h2Mem = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_H2,
                                         jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE;")

  @JvmStatic val hsqlMem = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_HSQL,
                                           jdbcUrl = "jdbc:hsqldb:mem:test",
                                           username = "sa",
                                           testQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
}