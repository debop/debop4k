package debop4k.spring.jdbc.config

import debop4k.spring.jdbc.core.config.embeddedDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

@Configuration
open class DataConfiguration {

  @Bean
  open fun dataSource(): DataSource {
    return embeddedDatabase(H2) {
      script(location = "classpath:db/schema-h2.sql")
      script(location = "classpath:db/data-h2.sql")
    }
  }

  @Bean
  open fun jdbcTemplate(): JdbcTemplate {
    return JdbcTemplate(dataSource())
  }

  @Bean
  open fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
    return NamedParameterJdbcTemplate(dataSource())
  }

  @Bean
  open fun transactionManager(): PlatformTransactionManager {
    return DataSourceTransactionManager(dataSource())
  }

  @Bean
  open fun transactionTemplate(): TransactionTemplate {
    return TransactionTemplate(transactionManager())
  }
}