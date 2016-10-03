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

import debop4k.config.database.DatabaseSetting
import debop4k.core.loggerOf
import debop4k.data.DataSources
import org.apache.tomcat.jdbc.pool.PoolProperties
import org.slf4j.Logger
import javax.sql.DataSource

/**
 * Tomcat Connection Pool 을 이용한 DataSource 를 생성합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class TomcatDataSourceFactory : DataSourceFactory {

  private val log: Logger = loggerOf(javaClass)

  /**
   * [DataSource] 를 생성합니다.
   * @param setting 데이터베이스 연결 정보
   * @return 생성된 [DataSource] 인스턴스. 실패 시 null 반환
   */
  override fun create(setting: DatabaseSetting): DataSource {
    log.info("Tomcat DataSource를 빌드합니다. {}", setting)

    val p = PoolProperties().apply {
      driverClassName = setting.driverClass
      url = setting.jdbcUrl
      username = setting.username
      password = setting.password

      initialSize = DataSources.MIN_POOL_SIZE
      maxActive = DataSources.MAX_POOL_SIZE
      maxIdle = DataSources.MIN_POOL_SIZE
      minIdle = DataSources.MIN_IDLE_SIZE

      timeBetweenEvictionRunsMillis = 30000
      minEvictableIdleTimeMillis = 30000
      validationQuery = setting.testQuery
      validationInterval = 60000
      isTestOnBorrow = true
      maxWait = 60000
      isLogAbandoned = true
      isRemoveAbandoned = true
    }

    log.info("Tomcat DataSource 설정 정보={}", p)
    return org.apache.tomcat.jdbc.pool.DataSource(p)
  }
}