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

package debop4k.data.mybatis.kotlin.config

import debop4k.data.mybatis.config.AbstractPostgresFlywayMyBatisConfiguration
import debop4k.data.mybatis.kotlin.mappers.KotlinActorMapper
import debop4k.data.mybatis.kotlin.repository.KotlinActorRepository
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.ComponentScan

/**
 * MyBatisConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@ComponentScan(basePackageClasses = arrayOf(KotlinActorRepository::class))
@MapperScan(basePackageClasses = arrayOf(KotlinActorMapper::class))
open class KotlinMyBatisConfiguration : AbstractPostgresFlywayMyBatisConfiguration() {

  override fun cleanDatabaseForTest(): Boolean {
    return true
  }

  override fun setupSqlSessionFactory(sf: SqlSessionFactoryBean) {
    super.setupSqlSessionFactory(sf)
    //    sf.setTypeHandlersPackage("debop4k.data.mybatis.typehandlers");
  }

  override fun getMyBatisConfigPath(): String {
    return "classpath:mybatis/mybatis-config.xml"
  }

  override fun getMyBatisMapperPath(): String {
    return "classpath:mybatis/mappers/**/*Mapper.xml"
  }
}
