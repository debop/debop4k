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

package debop4k.data.mybatis.config

import debop4k.core.loggerOf
import org.flywaydb.core.Flyway
import javax.annotation.PostConstruct

/**
 * MyBatis 를 사용하기 위한 Spring Framework 용 기본 Configuration 입니다.
 * [Flyway] 를 이용하여, 대상 Database 버전을 관리할 수 있습니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractFlywayMyBatisConfiguration : AbstractMyBatisConfiguration() {

  private val log = loggerOf(javaClass)

  /**
   * Test 를 위해 DB 를 처음부터 새로 SetUp 할 것인가? : 운영 중에는 절대 해서는 안됩니다.
   */
  open protected fun cleanDatabaseForTest(): Boolean {
    return false
  }

  /**
   * Flyway 를 이용한 DB Migration 을 수행하도록 합니다.
   */
  @PostConstruct
  open fun migrateDatabase() {
    log.debug("Flyway를 이용하여 Database Migration 작업을 시작합니다...")
    val flyway = Flyway()
    flyway.dataSource = dataSource()

    if (cleanDatabaseForTest()) {
      log.debug("Database 초기화 작업 수행")
      flyway.clean()
    }

    log.debug("Database migration 작업 수행 ...")
    flyway.migrate()
    log.debug("Database migration 작업 완료")
  }
}