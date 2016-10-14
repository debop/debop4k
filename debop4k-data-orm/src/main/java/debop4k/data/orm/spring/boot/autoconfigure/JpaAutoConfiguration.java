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

package debop4k.data.orm.spring.boot.autoconfigure;

import debop4k.data.orm.hibernate.dao.HibernateDao;
import debop4k.data.orm.hibernate.dao.HibernateQueryDslDao;
import debop4k.data.orm.jpa.dao.JpaDao;
import debop4k.data.orm.jpa.dao.JpaQuerydslDao;
import debop4k.data.spring.boot.autoconfigure.HikariDataSourceAutoConfiguration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({HibernateProperties.class})
public class JpaAutoConfiguration extends HikariDataSourceAutoConfiguration {

  @Inject HibernateProperties hibernateProps;
  @Inject DataSource dataSource;

  /**
   * 추가 작업 시 override 해서 사용하세요.
   */
  protected void setupEntityManagerFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
    //factoryBean.setMappingResources("META-INF/orm.xml");
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setGenerateDdl(true);
    return adapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    log.info("EntityManagerFactory 생성을 시작합니다...");

    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    String[] packageNames = hibernateProps.getMappedPakageNames();
    if (packageNames != null && packageNames.length > 0) {
      log.debug("JPA용 엔티티를 스캔합니다. packageNames={}", Arrays.toString(packageNames));
      factoryBean.setPackagesToScan(packageNames);
    }

    factoryBean.setJpaProperties(hibernateProps.toProperties());
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    factoryBean.setDataSource(dataSource);

    setupEntityManagerFactory(factoryBean);

    factoryBean.afterPropertiesSet();
    log.debug("EntityManagerFactory 를 생성했습니다.");

    return factoryBean;
  }

//  @Bean
//  public StatelessSessionFactoryBean statelessSessionFactory(EntityManagerFactory emf) {
//    return new StatelessSessionFactoryBean((SessionFactory)emf);
//  }

  @Bean
  @Override
  @NonNull
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager(entityManagerFactory().getObject());
  }

  @Bean
  public PersistenceExceptionTranslator exceptionTranslator() {
    return new HibernateExceptionTranslator();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public HibernateDao hibernateDao() {
    return new HibernateDao();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public HibernateQueryDslDao hibernateQueryDslDao() {
    return new HibernateQueryDslDao();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public JpaDao jpaDao() {
    return new JpaDao();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public JpaQuerydslDao jpaQuerydslDao() {
    return new JpaQuerydslDao();
  }
}
