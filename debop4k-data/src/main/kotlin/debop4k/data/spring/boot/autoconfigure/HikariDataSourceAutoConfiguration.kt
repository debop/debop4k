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

package debop4k.data.spring.boot.autoconfigure

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.core.utils.Objects
import debop4k.data.DataSources
import debop4k.data.jdbc.JdbcDao
import net.sf.log4jdbc.Log4jdbcProxyDataSource
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.*
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.sql.DataSource

/**
 * HikariDataSourceAutoConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableConfigurationProperties(HikariDataSourceProperties::class)
open class HikariDataSourceAutoConfiguration {

  private val log = loggerOf(javaClass)

  @Inject private val dataSourceProps: HikariDataSourceProperties = uninitialized()
  @Inject private val dataSource: DataSource = uninitialized()

  @Primary
  @Bean(name = arrayOf("dataSource"))
  open fun dataSource(): DataSource {
    return DataSources.of(dataSourceProps)
  }

  @Bean(name = arrayOf("dataSource"))
  @Profile("all", "default", "dev", "develop", "test", "testing")
  open fun log4jdbcProxyDataSource(): DataSource {
    return Log4jdbcProxyDataSource(DataSources.of(dataSourceProps))
  }

  @Bean
  open protected fun jdbcTemplate(): JdbcTemplate {
    return JdbcTemplate(dataSource)
  }

  @Bean
  open protected fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
    return NamedParameterJdbcTemplate(dataSource)
  }

  @Bean
  open protected fun transactionManager(): PlatformTransactionManager {
    return DataSourceTransactionManager(dataSource)
  }

  @Bean
  open fun exceptionTranslationPostProcessor(): BeanPostProcessor {
    return PersistenceExceptionTranslationPostProcessor()
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  open protected fun jdbcDao(dataSource: DataSource, template: NamedParameterJdbcTemplate): JdbcDao {
    return JdbcDao(dataSource, template)
  }

  /**
   * Flyway 를 이용한 DB Migration 을 수행하도록 합니다.
   */
  @PostConstruct
  open protected fun setupDatabase() {
    if (dataSourceProps.flyway == null) {
      return
    }

    val initMethod = dataSourceProps.flyway!!.initMethod
    if (initMethod.isEmpty() || "none" == initMethod.toLowerCase().trim()) {
      // 아무일도 하지 않고 지나갑니다
      return
    }

    log.debug("Flyway 를 이용하여 Database 셋업을 수행합니다...")
    val flyway = Flyway()
    flyway.dataSource = dataSource

    if (dataSourceProps.flyway!!.isClearDatabase) {
      flyway.clean()
    }

    val baselineOnMigrate = dataSourceProps.flyway!!.isBaselineOnMigrate
    log.info("Flyway 를 이용하여 database에 다음의 작업을 수행합니다. 작업={}, baselineOnMigrate={}",
             initMethod, baselineOnMigrate)

    if (initMethod.isNotEmpty()) {
      val method = initMethod.toLowerCase()
      if (Objects.equals(method, "migrate")) {
        flyway.isBaselineOnMigrate = baselineOnMigrate
        flyway.migrate()
      } else if (Objects.equals(method, "baseline")) {
        flyway.baseline()
      } else {
        log.warn("해당하는 작업이 없습니다. initMethod={}", initMethod)
      }
    }
  }
}