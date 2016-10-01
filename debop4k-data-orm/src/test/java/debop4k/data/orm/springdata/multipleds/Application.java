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

package debop4k.data.orm.springdata.multipleds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * 서로 다른 DataSource 를 가지는 엔티티에 대해 통합적으로 처리할 수 있도록 합니다.
 */
// 자동으로 처리하는 부분 중, 사용자가 정의한 부분은 제외합니다.
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    FlywayAutoConfiguration.class
})
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {

  @Inject DataInitializer initializer;

  @PostConstruct
  public void init() {
    Integer customerId = initializer.initializeCustomer();
//    initializer.initializeOrder(customerId);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
