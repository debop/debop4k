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

package debop4k.data.config

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import org.flywaydb.core.Flyway
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.sql.DataSource

/**
 * [DataSource]를 Bean 으로 제공하고, [Flyway] 를 이용하여 Database 를 Setup 해주는 Spring Configuration 입니다.
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractFlywayDataSourceConfiguration : AbstractDataSourceConfiguration() {

  private val log = loggerOf(javaClass)

  @Inject val dataSource: DataSource = uninitialized()

  /**
   * Test 를 위해 DB 를 처음부터 새로 SetUp 할 것인가? : 운영 중에는 절대 해서는 안됩니다.
   */
  protected fun cleanDatabaseForTest(): Boolean {
    return false
  }

  /**
   * [Flyway] 를 이용한 DB Migration 을 수행하도록 합니다.
   */
  @PostConstruct
  fun migrateDatabase() {
    log.info("Flyway를 이용하여 Database Migration 작업을 시작합니다...")

    try {
      val flyway = Flyway()
      flyway.dataSource = dataSource

      if (cleanDatabaseForTest()) {
        log.debug("Database 초기화 작업 수행을 수행합니다...")
        flyway.clean()
      }

      log.debug("Database migration 작업을 시작합니다...")
      flyway.migrate()
      log.info("Database migration 작업 완료")
    } catch(ignored: Throwable) {
      log.error("Flyway를 이용한 DB Migration 작업에 실패했습니다.", ignored)
    }
  }

}