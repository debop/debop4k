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

import debop4k.config.database.DatabaseSetting
import debop4k.data.DataSources

/**
 * H2 Database 접속을 위한 MyBatis 환경설정
 */
abstract class AbstractH2MyBatisConfiguration : AbstractMyBatisConfiguration() {

  open override fun getDatabaseSetting(): DatabaseSetting {
    return DataSources.h2Mem
  }
}

/**
 * H2 Database 접속을 위한 MyBatis 환경설정
 * [Flyway] 를 이용한 Database Migration 수행
 */
abstract class AbstractH2FlywayMyBatisConfiguration : AbstractFlywayMyBatisConfiguration() {

  open override fun getDatabaseSetting(): DatabaseSetting {
    return DataSources.h2Mem
  }

}



