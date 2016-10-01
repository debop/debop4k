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

package debop4k.data.orm.jpa.config.databases;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 테스트용 Spring Configuration 입니다.
 * 원하는 Database 용 Configuration 을 변경하고 테스트 하시면 됩니다.
 * JpaH2Configuration, JpaPostgreSqlConnection 등
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement(proxyTargetClass = true)
public abstract class JpaTestConfiguration extends JpaH2Configuration {

  @Override
  protected void setupEntityManagerFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
    super.setupEntityManagerFactory(factoryBean);
    // spring data 에서 제공하는 Auditable 에 대한 예제를 위해
    factoryBean.setMappingResources("META-INF/orm.xml");
  }

  @Override
  abstract public String[] getMappedPackageNames();

}
