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

package debop4k.config.database

import com.typesafe.config.Config
import debop4k.config.*

/**
 * DatabaseConfigElement
 * @author debop sunghyouk.bae@gmail.com
 */
open class DatabaseConfigElement(override val config: Config) : ConfigSupport, UserCredentialConfigElement {

  val host: String by lazy { config.loadString("host", "localhost")!! }

  val name: String by lazy { config.loadString("name", "")!! }

  val driverClass: String by lazy { config.loadString("driverClass", "")!! }

  val jdbcUrl: String by lazy { config.loadString("jdbcUrl", "")!! }

  val maxPoolSize: Int by lazy { config.loadInt("maxPoolSize", MAX_POOL_SIZE) }

  val minIdleSize: Int by lazy { config.loadInt("minIdleSize", MIN_IDLE_SIZE) }

  val testQuery: String by lazy { config.loadString("testQuery", TEST_QUERY)!! }

  val props: Map<String, String> by lazy { config.asMap() }


  val databaseSetting: DatabaseSetting
    get() = DatabaseSetting(host,
                            name,
                            driverClass,
                            jdbcUrl,
                            username,
                            password,
                            maxPoolSize,
                            minIdleSize,
                            testQuery,
                            props)


  companion object {
    @JvmStatic private val PROCESS_COUNT = Runtime.getRuntime().availableProcessors()
    @JvmStatic val MAX_POOL_SIZE = PROCESS_COUNT * 16
    @JvmStatic val MIN_POOL_SIZE = PROCESS_COUNT * 2
    @JvmStatic val MIN_IDLE_SIZE = if (PROCESS_COUNT > 2) 2 else PROCESS_COUNT

    @JvmStatic val TEST_QUERY: String = "SELECT 1"
  }
}