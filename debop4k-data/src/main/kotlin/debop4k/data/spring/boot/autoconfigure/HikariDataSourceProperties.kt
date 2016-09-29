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

import debop4k.core.uninitialized
import debop4k.data.DataSources
import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author sunghyouk.bae@gmail.com
 */
@ConfigurationProperties(prefix = "debop4k.datasource")
data class HikariDataSourceProperties(

    var driverClassName: String? = uninitialized(),
    var jdbcUrl: String? = uninitialized(),
    var username: String? = uninitialized(),
    var password: String? = uninitialized(),
    var maxPoolSize: Int? = DataSources.MAX_POOL_SIZE,
    var minIdleSize: Int? = DataSources.MIN_IDLE_SIZE,
    var testQuery: String? = "SELECT 1",

    var flyway: Flyway? = uninitialized(),
    var postgres: Postgres? = uninitialized()

                                     ) {

  data class Flyway(var clearDatabase: Boolean = false,
                    var initMethod: String = "",
                    var baselineOnMigrate: Boolean = false)

  data class Postgres(var preparedStatementCacheQueries: Int = 1024,
                      var preparedStatementCacheSize: Int = 64,
                      var tcpKeepAlive: Boolean = true,
                      var charset: String = "UTF-8") {

    fun toMap(): MutableMap<String, String> {
      val map = Maps.mutable.of<String, String>()
      map.put("preparedStatementCacheQueries", preparedStatementCacheQueries.toString())
      map.put("preparedStatementCacheSizeMiB", preparedStatementCacheSize.toString())
      map.put("tcpKeepAlive", tcpKeepAlive.toString())
      map.put("charset", charset)

      return map
    }
  }
}