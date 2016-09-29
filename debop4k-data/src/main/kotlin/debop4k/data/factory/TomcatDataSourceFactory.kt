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

import debop4k.data.DataSources.MAX_POOL_SIZE
import debop4k.data.DataSources.MIN_IDLE_SIZE
import debop4k.data.DataSources.MIN_POOL_SIZE
import debop4k.data.DatabaseSetting
import org.apache.tomcat.jdbc.pool.PoolProperties
import org.slf4j.LoggerFactory
import javax.sql.DataSource

/**
 * Tomcat Database Connection Pool 을 이용한 DataSource 를 생성합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class TomcatDataSourceFactory : DataSourceFactory {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun create(setting: DatabaseSetting): DataSource {
    log.info("Tomcat DataSource를 빌드합니다. {}", setting)

    val p = PoolProperties().apply {
      driverClassName = setting.driverClassName
      url = setting.jdbcUrl
      username = setting.username
      password = setting.password

      initialSize = MIN_POOL_SIZE
      maxActive = MAX_POOL_SIZE
      maxIdle = MIN_POOL_SIZE
      minIdle = MIN_IDLE_SIZE

      timeBetweenEvictionRunsMillis = 30000
      minEvictableIdleTimeMillis = 30000
      validationQuery = "SELECT 1"
      validationInterval = 34000
      isTestOnBorrow = true
      maxWait = 10000
      isLogAbandoned = true
      isRemoveAbandoned = true
    }

    log.info("Tomcat DataSource 설정 정보={}", p)
    return org.apache.tomcat.jdbc.pool.DataSource(p)
  }
}