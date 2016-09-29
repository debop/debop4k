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
import debop4k.config.UserCredentialConfigElement
import debop4k.config.asMap
import debop4k.config.loadInt
import debop4k.config.loadString
import org.eclipse.collections.api.map.MutableMap

/**
 * Database 접속을 위한 환경설정 정보
 *
 * @author sunghyouk.bae@gmail.com
 */
open class DatabaseConfigElement(override val config: Config) : UserCredentialConfigElement {

  /** Database 서버 주소 */
  val host: String by lazy { config.loadString("host", "localhost")!! }

  /** Database name */
  val name: String by lazy { config.loadString("name", "")!! }

  /** Driver class name */
  val driverClass: String by lazy { config.loadString("driverClass", "")!! }

  /** jdbc url */
  val jdbcUrl: String by lazy { config.loadString("jdbcUrl", "")!! }

  /**
   * maximum connection pool size
   * @see MAX_POOL_SIZE
   */
  val maxPoolSize: Int by lazy { config.loadInt("maxPoolSize", MAX_POOL_SIZE) }
  /**
   * minimum connection idle size
   * @see MIN_IDLE_SIZE
   */
  val minIdleSize: Int by lazy { config.loadInt("minIdleSize", MIN_IDLE_SIZE) }

  /** Test query for idle connection */
  val testQuery: String by lazy { config.loadString("testQuery", TEST_QUERY)!! }

  /** connection properties */
  val props: MutableMap<String, String> by lazy { config.asMap() }

  /** Database Setting */
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

    /** CPU Core count */
    private val PROCESS_COUNT = Runtime.getRuntime().availableProcessors()

    /** Maximum connetion pool size ( cpu core * 8 ) */
    @JvmField val MAX_POOL_SIZE = PROCESS_COUNT * 8

    /** Minimum connection pool size ( cpu core * 2) */
    @JvmField val MIN_POOL_SIZE = PROCESS_COUNT * 2

    /** Minimum connection idle size ( 2 ~ cpu core ) */
    @JvmField val MIN_IDLE_SIZE = Math.min(2, PROCESS_COUNT)

    /** Test Query for idle connection */
    @JvmField val TEST_QUERY: String = "SELECT 1"
  }
}