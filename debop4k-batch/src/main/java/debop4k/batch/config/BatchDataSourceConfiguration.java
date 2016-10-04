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

import debop4k.data.DataSources;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * BatchDataSourceConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @see org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration
 */
@Slf4j
@Configuration
public class BatchDataSourceConfiguration {

  @Bean(name = "jobDataSource")
  public DataSource jobDataSource() {
    return DataSources.ofEmbeddedH2();
  }

  @Bean(name = "jobTransactionManager")
  public PlatformTransactionManager jobTransactionManager() {
    return new DataSourceTransactionManager(jobDataSource());
  }

  /**
   * TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
   * 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
   * 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.
   */
  @Bean(name = "jobTaskExecutor")
  @SneakyThrows({Exception.class})
  @Profile({"default", "test", "local"})
  public TaskExecutor emptyJobTaskExecutor() {
    return null;
  }

  @Bean(name = "jobTaskExecutor")
  @SneakyThrows({Exception.class})
  public TaskExecutor jobTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
    executor.afterPropertiesSet();
    return executor;
  }
}
