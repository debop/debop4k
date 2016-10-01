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

package debop4k.data.mybatis.cache;

import debop4k.data.mybatis.config.MyBatisConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

/**
 * EhCache 에 대한 환경설정을 Resources 의 ehcache.xml 정보를 활용하는 환경설정입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@EnableCaching(proxyTargetClass = true)
@ComponentScan(basePackageClasses = {EhCacheActorRepository.class})
public class EhCacheConfigurationByXml extends MyBatisConfiguration {

  @Bean
  public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
    EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
    factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
    factoryBean.setShared(true);

    return factoryBean;
  }

  @Bean
  public CacheManager ehcacheCacheManagerFromXml(EhCacheManagerFactoryBean cacheManagerFactoryBean) {
    return new EhCacheCacheManager(cacheManagerFactoryBean.getObject());
  }
}
