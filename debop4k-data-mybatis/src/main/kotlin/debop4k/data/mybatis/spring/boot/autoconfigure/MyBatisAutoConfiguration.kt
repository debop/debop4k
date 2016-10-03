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

package debop4k.data.mybatis.spring.boot.autoconfigure

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.data.spring.boot.autoconfigure.HikariDataSourceAutoConfiguration
import org.apache.ibatis.mapping.DatabaseIdProvider
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.mapper.ClassPathMapperScanner
import org.mybatis.spring.mapper.MapperFactoryBean
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.autoconfigure.AutoConfigurationPackages
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ResourceLoaderAware
import org.springframework.context.annotation.*
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.ObjectUtils
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.sql.DataSource

/**
 * MyBatis 를 위한 Spring Boot AutoConfiguration 파일
 * @author debop sunghyouk.bae@gmail.com
 */
@Configuration
@EnableConfigurationProperties(MyBatisProperties::class)
@AutoConfigureAfter(HikariDataSourceAutoConfiguration::class)
open class MyBatisAutoConfiguration {

  private val log = loggerOf(javaClass)

  @Inject val properties: MyBatisProperties = uninitialized()
  @Inject val resourceLoader = DefaultResourceLoader()

  @Autowired(required = false) val interceptors: Array<Interceptor>? = uninitialized()
  @Autowired(required = false) val databaseIdProvider: DatabaseIdProvider? = uninitialized()

  @PostConstruct
  open fun checkConfigFileExists() {
    if (properties.checkConfiguration && !properties.configLocation.isNullOrEmpty()) {
      val resource = this.resourceLoader.getResource(properties.configLocation)
      assert(resource.exists()) {
        "Cannot found config location: $resource (please add config file or check your MyBatis Configuration"
      }
    }
  }

  @Bean
  open fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
    log.info("SqlSessionFactory를 빌드합니다...")
    val factoryBean = SqlSessionFactoryBean()

    factoryBean.setDataSource(dataSource)
    factoryBean.vfs = SpringBootVFS::class.java

    if (!properties.configLocation.isNullOrEmpty()) {
      factoryBean.setConfigLocation(this.resourceLoader.getResource(properties.configLocation))
    }

    // factoryBean.setConfiguration 는 MyBatis 3.4.x 이상에서 사용하는데,
    // 테스트 시 문제가 있다.
    // 그래서 MyBatis 3.3.x 를 사용하고, setConfiguration 호출을 주석처리 했습니다.
    //    factoryBean.setConfiguration(properties.getConfiguration());

    if (!ObjectUtils.isEmpty(this.interceptors)) {
      factoryBean.setPlugins(interceptors)
    }

    if (databaseIdProvider != null) {
      factoryBean.databaseIdProvider = databaseIdProvider
    }

    if (!properties.typeAliasPackage.isNullOrEmpty()) {
      factoryBean.setTypeAliasesPackage(properties.typeAliasPackage)
    }

    if (!properties.typeHandlersPackage.isNullOrEmpty()) {
      factoryBean.setTypeHandlersPackage(properties.typeHandlersPackage)
    }

    if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
      factoryBean.setMapperLocations(properties.resolveMapperLocations())
    }

    log.debug("SqlSessionFactory를 빌드했습니다.")
    return factoryBean.`object`
  }

  @Bean
  @ConditionalOnMissingBean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  open fun sqlSessionTemplate(sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
    log.debug("Create SqlSessionTemplate instance...")

    if (properties.executorType != null) {
      return SqlSessionTemplate(sqlSessionFactory, properties.executorType)
    } else {
      return SqlSessionTemplate(sqlSessionFactory)
    }
  }

  /**
   * This will just scan the same base package as Spring Boot does. If you want
   * more power, you can explicitly use
   * [org.mybatis.spring.annotation.MapperScan] but this will get typed
   * mappers working correctly, out-of-the-box, similar to using Spring Data JPA
   * repositories.
   */
  open class AutoConfiguredMapperScannerRegistrar : BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private val log = loggerOf(javaClass)

    private var beanFactory: BeanFactory? = uninitialized()
    private var resourceLoader: ResourceLoader? = uninitialized()

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
      log.debug("Searching for mappers annotated with @Mapper")

      val scanner = ClassPathMapperScanner(registry)

      try {
        if (this.resourceLoader != null) {
          scanner.resourceLoader = this.resourceLoader
        }

        val packages = AutoConfigurationPackages.get(this.beanFactory!!)
        if (log.isTraceEnabled) {
          packages.forEach {
            log.trace("Using auto-configuration base package '{}'", it)
          }
        }

        if (packages.isNotEmpty()) {
          // scanner.setAnnotationClass 는 MyBatis 3.4.x 이상에서 사용하는데,
          // 테스트 시 문제가 있다.
          // 그래서 MyBatis 3.3.x 를 사용하고, setAnnotationClass 호출을 주석처리 했습니다.
          //          scanner.setAnnotationClass(Mapper.class);
          scanner.registerFilters()
          scanner.doScan(*packages.toTypedArray())
        }
      } catch (ignored: IllegalStateException) {
        log.warn("auto-configuration package 를 결정하지 못했습니다. automatic mapper scanning 을 할 수 없습니다.", ignored)
      }
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
      this.beanFactory = beanFactory
    }

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
      this.resourceLoader = resourceLoader
    }
  }

  /**
   * [org.mybatis.spring.annotation.MapperScan] ultimately ends up
   * creating instances of [MapperFactoryBean]. If
   * [org.mybatis.spring.annotation.MapperScan] is used then this
   * auto-configuration is not needed. If it is _not_ used, however, then this
   * will bring in a bean registrar and automatically register components based
   * on the same component-scanning path as Spring Boot itself.
   */
  @Configuration
  @Import(AutoConfiguredMapperScannerRegistrar::class)
  open class MapperScannerRegistrarNotFoundConfiguration {

    private val log = loggerOf(javaClass)

    @PostConstruct
    open fun afterPropertiesSet() {
      log.debug("No {} found.", MapperFactoryBean::class.qualifiedName)
    }
  }
}