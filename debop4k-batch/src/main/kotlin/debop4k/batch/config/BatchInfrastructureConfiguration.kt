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

package debop4k.batch.config

import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.configuration.support.MapJobRegistry
import org.springframework.batch.core.converter.JobParametersConverter
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.launch.support.SimpleJobOperator
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.*
import org.springframework.core.task.TaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * BatchInfrastructureConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@Import(BatchDataSourceConfiguration::class)
open class BatchInfrastructureConfiguration {

  private val log = LoggerFactory.getLogger(javaClass)

  @Autowired(required = false)
  private val jobParametersConverter: JobParametersConverter? = null

  @Bean
  open fun jobOperator(jobLauncher: JobLauncher,
                       jobRepo: JobRepository,
                       jobExplorer: JobExplorer,
                       jobRegistry: JobRegistry): JobOperator {
    log.info("JobOperator를 생성합니다...")
    val operator = SimpleJobOperator()
    operator.setJobLauncher(jobLauncher)
    operator.setJobRepository(jobRepo)
    operator.setJobExplorer(jobExplorer)
    operator.setJobRegistry(jobRegistry)

    operator.afterPropertiesSet()

    log.info("JobOperator를 생성했습니다.")
    return operator
  }

  @Bean
  open fun jobRegistryBeanPostProcessor(jobRegistry: JobRegistry): JobRegistryBeanPostProcessor {
    val postProcessor = JobRegistryBeanPostProcessor()
    postProcessor.setJobRegistry(jobRegistry)

    return postProcessor
  }

  @Bean
  open fun jobRegistry(): JobRegistry {
    return MapJobRegistry()
  }

  @Bean
  open fun jobExplorer(@Qualifier("jobDataSource") jobDs: DataSource): JobExplorer {
    val jobExplorer = JobExplorerFactoryBean()
    jobExplorer.setDataSource(jobDs)
    jobExplorer.afterPropertiesSet()

    return jobExplorer.`object`
  }

  @Bean
  open fun jobRepository(@Qualifier("jobDataSource") jobDs: DataSource,
                         @Qualifier("jobTransactionManager") jobTm: PlatformTransactionManager): JobRepository {
    log.info("JobRepository 생성...")
    val factory = JobRepositoryFactoryBean()
    factory.setDataSource(jobDs)
    factory.transactionManager = jobTm

    // HINT: 에러메시지 Standard JPA does not support custom isolation levels - use a special JpaDialect for your JPA implementation 가 나올 때
    // HINT: http://forum.springsource.org/showthread.php?59779-Spring-Batch-1-1-2-Standard-JPA-does-not-support-custom-isolation-levels-use-a-sp
    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT")

    factory.afterPropertiesSet()

    log.info("JobRepository 생성 완료")
    return factory.`object`
  }

  @Bean
  open fun jobLauncher(jobRepo: JobRepository,
                       @Autowired @Qualifier("jobTaskExecutor") executor: TaskExecutor?): JobLauncher {
    val launcher = SimpleJobLauncher()
    launcher.setJobRepository(jobRepo)

    if (executor != null) {
      launcher.setTaskExecutor(executor)
    }
    return launcher
  }

  @Bean
  open fun jobBuilderFactory(jobRepo: JobRepository): JobBuilderFactory {
    return JobBuilderFactory(jobRepo)
  }

  @Bean
  open fun stepBuilderFactory(jobRepo: JobRepository,
                              @Qualifier("jobTransactionManager") jobTm: PlatformTransactionManager): StepBuilderFactory {
    return StepBuilderFactory(jobRepo, jobTm)
  }
}