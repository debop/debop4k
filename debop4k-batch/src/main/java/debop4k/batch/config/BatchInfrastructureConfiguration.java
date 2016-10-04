/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.batch.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Batch 작업을 위한 필수 Bean 을 나타냅니다.
 * JobRegistry, JobRepository, JobExplorer, JobLauncher 등을 Bean 으로 제공합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @see BatchAutoConfiguration
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchInfrastructureConfiguration {

  @Autowired(required = false)
  private JobParametersConverter jobParametersConverter;

  @Bean
  public JobOperator jobOperator(JobLauncher jobLauncher,
                                 JobRepository jobRepo,
                                 JobExplorer jobExplorer,
                                 JobRegistry jobRegistry) throws Exception {
    log.info("JobOperator를 생성합니다...");
    SimpleJobOperator operator = new SimpleJobOperator();
    operator.setJobLauncher(jobLauncher);
    operator.setJobRepository(jobRepo);
    operator.setJobExplorer(jobExplorer);
    operator.setJobRegistry(jobRegistry);

    operator.afterPropertiesSet();

    log.info("JobOperator를 생성했습니다.");
    return operator;
  }

  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
    JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
    postProcessor.setJobRegistry(jobRegistry);

    return postProcessor;
  }

  @Bean
  public JobRegistry jobRegistry() {
    return new MapJobRegistry();
  }

  @Bean
  @SneakyThrows
  public JobExplorer jobExplorer(@Qualifier("jobDataSource") DataSource jobDs) {
    JobExplorerFactoryBean jobExplorer = new JobExplorerFactoryBean();
    jobExplorer.setDataSource(jobDs);
    jobExplorer.afterPropertiesSet();

    return jobExplorer.getObject();
  }

  @Bean
  @SneakyThrows
  public JobRepository jobRepository(@Qualifier("jobDataSource") DataSource jobDs,
                                     @Qualifier("jobTransactionManager") PlatformTransactionManager jobTm) {
    log.info("JobRepository 생성...");
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(jobDs);
    factory.setTransactionManager(jobTm);

    // HINT: 에러메시지 Standard JPA does not support custom isolation levels - use a special JpaDialect for your JPA implementation 가 나올 때
    // HINT: http://forum.springsource.org/showthread.php?59779-Spring-Batch-1-1-2-Standard-JPA-does-not-support-custom-isolation-levels-use-a-sp
    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");

    factory.afterPropertiesSet();

    log.info("JobRepository 생성 완료");
    return factory.getObject();
  }

  @Bean
  public JobLauncher jobLauncher(JobRepository jobRepo,
                                 @Qualifier("jobTaskExecutor") TaskExecutor executor) {
    SimpleJobLauncher launcher = new SimpleJobLauncher();
    launcher.setJobRepository(jobRepo);

    if (executor != null) {
      launcher.setTaskExecutor(executor);
    }
    return launcher;
  }

  @Bean
  public JobBuilderFactory jobBuilderFactory(JobRepository jobRepo) {
    return new JobBuilderFactory(jobRepo);
  }

  @Bean
  @SneakyThrows({Exception.class})
  public StepBuilderFactory stepBuilderFactory(JobRepository jobRepo,
                                               @Qualifier("jobTransactionManager") PlatformTransactionManager jobTm) {
    return new StepBuilderFactory(jobRepo, jobTm);
  }
}
