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

package debop4k.data.mybatis.config

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.data.config.AbstractDataSourceConfiguration
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import javax.inject.Inject

/**
 * MyBatis 를 사용하기 위한 Spring Framework 용 기본 Configuration 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractMyBatisConfiguration : AbstractDataSourceConfiguration() {

  private val log = loggerOf(javaClass)

  @Inject val appContext: ApplicationContext = uninitialized()

  open protected fun getMyBatisConfigPath(): String {
    return "" //"classpath:mybatis/config.xml";
  }

  open protected fun getMyBatisMapperPath(): String {
    return "" //"classpath:mybatis/mappers/**/*Mapper.xml";
  }

  open protected fun setupSqlSessionFactory(sf: SqlSessionFactoryBean) {
    //
  }

  /**
   * MyBatis [SqlSessionFactory] Bean
   */
  @Bean
  open protected fun sqlSessionFactory(): SqlSessionFactory {
    log.info("SqlSessionFactory를 빌드합니다.")
    val sf = SqlSessionFactoryBean()

    sf.setFailFast(true)
    sf.setDataSource(dataSource())

    if (getMyBatisConfigPath().isNotEmpty()) {
      sf.setConfigLocation(appContext.getResource(getMyBatisConfigPath()))
    }

    if (getMyBatisMapperPath().isNotEmpty()) {
      sf.setMapperLocations(appContext.getResources(getMyBatisMapperPath()))
    }

    setupSqlSessionFactory(sf)

    sf.afterPropertiesSet()

    log.info("SqlSessionFactory 를 빌드했습니다.")
    return sf.`object`
  }

  /**
   * MyBatis 용 [SqlSessionTemplate] Bean (매번 생성됩니다)
   */
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  open fun sqlSessionTemplate(sf: SqlSessionFactory): SqlSessionTemplate {
    return SqlSessionTemplate(sf)
  }

}