package debop4k.data.spring.boot.autoconfigure

import debop4k.core.uninitialized
import debop4k.data.DataSources
import debop4k.data.DatabaseSetting
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

  @Inject var dataSourceProps: HikariDataSourceProperties = uninitialized()
  @Inject var dataSource: DataSource = uninitialized()

  @Bean(name = arrayOf("dataSource"))
  open fun dataSource(): DataSource {
    println("dataSource properties=" + dataSourceProps)
    println("flyway=" + dataSourceProps.flyway)
    val setting = DatabaseSetting().apply {
      driverClassName = dataSourceProps.driverClassName ?: ""
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
    }
  }

}