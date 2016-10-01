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

package debop4k.data.orm.springdata.multipleds.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class},
                       entityManagerFactoryRef = "orderEntityManagerFactory",
                       transactionManagerRef = "orderTransactionManager")
public class OrderConfiguration {

  @Bean(name = "orderDataSource")
  @Primary
  DataSource orderDataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .setName("orders")
        .build();
  }

  @Bean(name = "orderEntityManagerFactory")
  LocalContainerEntityManagerFactoryBean orderEntityManagerFactory() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

    factoryBean.setDataSource(orderDataSource());
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    factoryBean.setPackagesToScan(Order.class.getPackage().getName());

    return factoryBean;
  }

  @Bean(name = "orderTransactionManager")
  PlatformTransactionManager orderTransactionManager() {
    return new JpaTransactionManager(orderEntityManagerFactory().getObject());
  }
}
