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

package debop4k.data.mybatis.config;

import debop4k.data.mybatis.mappers.ActorMapper;
import debop4k.data.mybatis.repository.ActorRepository;
import debop4k.data.mybatis.typehandlers.mappers.UUIDBeanMapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 사용을 위한 Spring 환경설정
 *
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@ComponentScan(basePackageClasses = {ActorRepository.class})
@MapperScan(basePackageClasses = {ActorMapper.class, UUIDBeanMapper.class})
public class MyBatisConfiguration extends AbstractH2FlywayMyBatisConfiguration {

  @Override
  protected boolean cleanDatabaseForTest() {
    return true;
  }

  @Override
  protected void setupSqlSessionFactory(SqlSessionFactoryBean sf) {
    super.setupSqlSessionFactory(sf);
//    sf.setTypeHandlersPackage("debop4k.data.mybatis.typehandlers");
  }

  @Override
  protected String getMyBatisConfigPath() {
    return "classpath:mybatis/mybatis-config.xml";
  }

  @Override
  protected String getMyBatisMapperPath() {
    return "classpath:mybatis/mappers/**/*Mapper.xml";
  }
}
