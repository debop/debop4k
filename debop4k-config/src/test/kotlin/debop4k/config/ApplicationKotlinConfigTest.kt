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

package debop4k.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.slf4j.LoggerFactory

/**
 * ApplicationKotlinConfigTest
 * @author debop sunghyouk.bae@gmail.com
 */
class ApplicationKotlinConfigTest {

  private val log = LoggerFactory.getLogger(javaClass)

  val steps = listOf("local", "devel", "test", "prod")

  @Test
  fun `load configurations`() {
    for (step in steps) {
      log.debug("step=$step")
      val config = ConfigLoader.load("config/$step", "application")
      val appConfig = ApplicationKotlinConfig(config)

      assertThat(appConfig.database.driverClass).isNotEmpty()
      assertThat(appConfig.database.jdbcUrl).isNotEmpty()

      assertThat(appConfig.redis.host).isEqualTo("127.0.0.1")
      assertThat(appConfig.redis.port).isEqualTo(6379)

      assertThat(appConfig.email.encoding).isEqualTo("UTF-8")
      assertThat(appConfig.email.protocol).isEqualTo("smtp")
      assertThat(appConfig.email.properties["mail.transport.protocol"]).isEqualTo("smtp")

      assertThat(appConfig.mongodb.database).isNotNull()

      assertThat(appConfig.hibernate.hbm2ddl).isNotNull()

      assertThat(appConfig.redisson.configPath).isNotEmpty()
      val redissonConfigPath = appConfig.redisson.configPath
      val inputStream = javaClass.classLoader.getResourceAsStream(redissonConfigPath)
      assertThat(inputStream).isNotNull()
    }
  }
}
