package debop4k.batch.config

import debop4k.data.spring.boot.autoconfigure.HikariDataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * KotlinBatchJobConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableAutoConfiguration(exclude = arrayOf(FlywayAutoConfiguration::class))
@Import(BatchDataSourceConfiguration::class, BatchInfrastructureConfiguration::class)
open class KotlinBatchJobConfiguration : HikariDataSourceAutoConfiguration() {
}