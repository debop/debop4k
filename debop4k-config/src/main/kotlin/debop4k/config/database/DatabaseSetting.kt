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

import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.map.mutable.UnifiedMap


/**
 * Database 접속을 위한 설정정보를 표현하는 ValueObject 입니다.
 */
data class DatabaseSetting(val host: String = "",
                           val name: String = "",
                           val driverClass: String = "",
                           val jdbcUrl: String = "",
                           val username: String? = null,
                           val password: String? = null,
                           val maxPoolSize: Int = DatabaseConfigElement.MAX_POOL_SIZE,
                           val minIdleSize: Int = DatabaseConfigElement.MIN_IDLE_SIZE,
                           val testQuery: String = DatabaseConfigElement.TEST_QUERY,
                           val props: MutableMap<String, String> = UnifiedMap<String, String>()) {
  companion object {
    @JvmStatic
    fun builder(): DatabaseSettingBuilder = DatabaseSettingBuilder()
  }

  /**
   * Builder for [DatabaseSetting]
   * Builder pattern 보다는 apply {} 를 사용하세요
   */
  class DatabaseSettingBuilder {

    var host: String = ""
    var name: String = ""
    var driverClass: String = ""
    var jdbcUrl: String = ""
    var username: String? = null
    var password: String? = null
    var maxPoolSize: Int = DatabaseConfigElement.MAX_POOL_SIZE
    var minIdleSize: Int = DatabaseConfigElement.MIN_IDLE_SIZE
    var testQuery: String = DatabaseConfigElement.TEST_QUERY
    var props: MutableMap<String, String> = UnifiedMap<String, String>()

    fun host(host: String): DatabaseSettingBuilder {
      this.host = host
      return this
    }

    fun name(name: String): DatabaseSettingBuilder {
      this.name = name
      return this
    }

    fun driverClass(driverClass: String): DatabaseSettingBuilder {
      this.driverClass = driverClass
      return this
    }

    fun jdbcUrl(jdbcUrl: String): DatabaseSettingBuilder {
      this.jdbcUrl = jdbcUrl
      return this
    }

    fun username(username: String): DatabaseSettingBuilder {
      this.username = username
      return this
    }

    fun password(password: String): DatabaseSettingBuilder {
      this.password = password
      return this
    }

    fun maxPoolSize(maxPoolSize: Int): DatabaseSettingBuilder {
      this.maxPoolSize = maxPoolSize
      return this
    }

    fun minIdleSize(minIdleSize: Int): DatabaseSettingBuilder {
      this.minIdleSize = minIdleSize
      return this
    }

    fun testQuery(testQuery: String): DatabaseSettingBuilder {
      this.testQuery = testQuery
      return this
    }

    fun props(props: MutableMap<String, String>): DatabaseSettingBuilder {
      this.props = props
      return this
    }

    fun build(): DatabaseSetting {
      return DatabaseSetting(this.host,
                             this.name,
                             this.driverClass,
                             this.jdbcUrl,
                             this.username,
                             this.password,
                             this.maxPoolSize,
                             this.minIdleSize,
                             this.testQuery,
                             this.props)
    }

  }
}