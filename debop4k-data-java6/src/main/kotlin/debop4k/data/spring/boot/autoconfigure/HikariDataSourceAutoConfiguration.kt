/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.data.spring.boot.autoconfigure

import debop4k.core.EMPTY_STRING
import debop4k.core.uninitialized
import debop4k.data.DataSources
import debop4k.data.DatabaseSetting
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(HikariDataSourceProperties::class)
open class HikariDataSourceAutoConfiguration {

  private val log = LoggerFactory.getLogger(javaClass)

  @Inject val dataSourceProps: HikariDataSourceProperties = uninitialized()
  @Inject val dataSource: DataSource = uninitialized()

  @Bean(name = arrayOf("dataSource"))
  open fun dataSource(): DataSource {
    log.debug("dataSource properties={}", dataSourceProps)
    log.debug("flyway={}", dataSourceProps.flyway)

    val setting = DatabaseSetting().apply {
      driverClassName = dataSourceProps.driverClassName ?: EMPTY_STRING
      jdbcUrl = dataSourceProps.jdbcUrl ?: ""
    }
    return DataSources.of(setting)
  }

  @Bean
  open fun jdbcTemplate(): JdbcTemplate = JdbcTemplate(dataSource)

  @Bean
  open fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate
      = NamedParameterJdbcTemplate(dataSource)

  @Bean
  open fun transactionManager(): PlatformTransactionManager
      = DataSourceTransactionManager(dataSource)

  @PostConstruct
  open fun setupDatabase() {
    if (dataSourceProps.flyway != null) {
      println("flyway is defined")

      // TODO : 구현 필요
    }
  }

}