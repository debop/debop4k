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

import debop4k.config.database.DatabaseSetting
import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.data.DataSources
import debop4k.data.jdbc.JdbcDao
import net.sf.log4jdbc.Log4jdbcProxyDataSource
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.inject.Inject
import javax.sql.DataSource

/**
 * [DataSource] Component를 제공하는 Spring Configuration 입니다.
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractDataSourceConfiguration {

  private val log = loggerOf(javaClass)

  @Inject private val dataSource: DataSource = uninitialized()

  open protected fun getDatabaseSetting(): DatabaseSetting = DataSources.h2Mem

  @Primary
  @Bean(name = arrayOf("dataSource"))
  open fun dataSource(): DataSource {
    return DataSources.of(getDatabaseSetting())
  }

  @Bean(name = arrayOf("dataSource"))
  @Profile("all", "default", "dev", "develop", "test", "testing")
  open fun log4jdbcProxyDataSource(): DataSource {
    return Log4jdbcProxyDataSource(DataSources.of(getDatabaseSetting()))
  }

  @Bean
  open fun jdbcTemplate(): JdbcTemplate {
    return JdbcTemplate(dataSource)
  }

  @Bean
  open fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
    return NamedParameterJdbcTemplate(dataSource)
  }

  @Bean
  open fun transactionManager(): PlatformTransactionManager {
    return DataSourceTransactionManager(dataSource)
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  open protected fun jdbcDao(dataSource: DataSource,
                             template: NamedParameterJdbcTemplate): JdbcDao {
    return JdbcDao(dataSource, template)
  }
}