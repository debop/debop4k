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

package debop4k.data.orm.hibernate.config;

import debop4k.data.config.AbstractDataSourceConfiguration;
import debop4k.data.orm.hibernate.HibernateEx;
import debop4k.data.orm.hibernate.dao.HibernateDao;
import debop4k.data.orm.hibernate.dao.HibernateQueryDslDao;
import debop4k.data.orm.hibernate.interceptors.PersistentObjectInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Spring Framework 용 Hibernate 환경 설정 Class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@EnableAspectJAutoProxy
@Configuration
public abstract class AbstractHibernateConfiguration extends AbstractDataSourceConfiguration {

  /**
   * 사용할 database 명
   *
   * @return database 명
   */
  public String getDatabaseName() {
    return "hibernate";
  }

  public abstract String[] getMappedPackageNames();

  /**
   * Hibernate 설정 정보
   *
   * @return Properties instance.
   */
  public Properties hibernateProperties() {
    return HibernateEx.newHibernateProperties();
  }

  /**
   * 추가 작업 시 override 해서 사용하세요.
   *
   * @param factoryBean Hibernate session factory bean
   */
  protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) { }

  @Bean
  @SneakyThrows(IOException.class)
  public LocalSessionFactoryBean sessionFactory() {

    log.info("SessionFactory 생성을 시작합니다...");

    LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
    String[] packageNames = getMappedPackageNames();
    if (packageNames != null && packageNames.length > 0) {
      log.debug("hibernate용 엔티티를 스캔합니다. packageNames={}", Arrays.toString(packageNames));
      factoryBean.setPackagesToScan(packageNames);
    }

    // JPA 는 @PostPersist, @PostLoad 로 PersistentObject#isPersisted 를 관리할 수 있지만,
    // Hibernate 는 interceptor 를 통해서 관리해야 합니다.
    factoryBean.setEntityInterceptor(new PersistentObjectInterceptor());
    factoryBean.setHibernateProperties(hibernateProperties());
    factoryBean.setDataSource(dataSource());
    setupSessionFactory(factoryBean);

    factoryBean.afterPropertiesSet();
    log.debug("SessionFactory 를 생성했습니다.");

    return factoryBean;
  }

  @Bean
  @Override
  public HibernateTransactionManager transactionManager() {
    return new HibernateTransactionManager(sessionFactory().getObject());
  }

  @Bean
  public HibernateExceptionTranslator exceptionTranslator() {
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

}
