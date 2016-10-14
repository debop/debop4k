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

package debop4k.data.orm.jpa.config;

import debop4k.data.config.AbstractDataSourceConfiguration;
import debop4k.data.orm.hibernate.HibernateEx;
import debop4k.data.orm.hibernate.dao.HibernateDao;
import debop4k.data.orm.hibernate.dao.HibernateQueryDslDao;
import debop4k.data.orm.jpa.dao.JpaDao;
import debop4k.data.orm.jpa.dao.JpaQuerydslDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Properties;

/**
 * Spring Framework 용 JPA 환경 설정 Class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
// NOTE: EnableAspectJAutoProxy 을 제대로 사용하려면, 직접 Bean으로 정의하면 안되고, JpaDao를 ComponentScan으로 등록해야 한다 (이유는 몰라...)
@Slf4j
@EnableAspectJAutoProxy
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public abstract class AbstractJpaConfiguration extends AbstractDataSourceConfiguration {

  public abstract String[] getMappedPackageNames();

  public String getDatabaseName() {
    return "hibernate";
  }

  public Properties jpaProperties() {
    return HibernateEx.newHibernateProperties();
  }

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
    String[] packageNames = getMappedPackageNames();
    if (packageNames != null && packageNames.length > 0) {
      log.debug("JPA용 엔티티를 스캔합니다. packageNames={}", Arrays.toString(packageNames));
      factoryBean.setPackagesToScan(packageNames);
    }

    factoryBean.setJpaProperties(jpaProperties());
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    factoryBean.setDataSource(dataSource());

    setupEntityManagerFactory(factoryBean);

    factoryBean.afterPropertiesSet();
    log.debug("EntityManagerFactory 를 생성했습니다.");

    return factoryBean;
  }

//  @Bean
//  public SessionFactory sessionFactory(EntityManagerFactory emf) {
//    return ((HibernateEntityManagerFactory) emf).getSessionFactory();
//  }

//  @Bean
//  public StatelessSessionFactoryBean statelessSessionFactory(SessionFactory sf) {
//    return new StatelessSessionFactoryBean(sf);
//  }

  @Bean
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
