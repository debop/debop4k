package debop4k.batch.config

import debop4k.data.DataSources
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.task.TaskExecutor
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * BatchDataSourceConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
open class BatchDataSourceConfiguration {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean(name = arrayOf("jobDataSource"))
  open fun jobDataSource(): DataSource {
    return DataSources.ofEmbeddedH2()
  }

  @Bean(name = arrayOf("jobTransactionManager"))
  open fun jobTransactionManager(): PlatformTransactionManager {
    return DataSourceTransactionManager(jobDataSource())
  }

  /**
   * TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
   * 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
   * 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.
   */
  @Bean(name = arrayOf("jobTaskExecutor"))
  @Profile("default", "test", "local")
  open fun emptyJobTaskExecutor(): TaskExecutor? {
    return null
  }

  @Bean(name = arrayOf("jobTaskExecutor"))
  open fun jobTaskExecutor(): TaskExecutor {
    val executor = ThreadPoolTaskExecutor()
    executor.maxPoolSize = Runtime.getRuntime().availableProcessors() * 2
    executor.afterPropertiesSet()
    return executor
  }

}