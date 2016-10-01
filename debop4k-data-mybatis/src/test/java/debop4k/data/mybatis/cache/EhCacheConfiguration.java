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
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * EhCache 환경설정을 Java Config 에서 설정한 예제입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@EnableCaching(proxyTargetClass = true)
@ComponentScan(basePackageClasses = {EhCacheActorRepository.class})
public class EhCacheConfiguration extends MyBatisConfiguration {

  /**
   * EhCache 사용을 위한 환경설정 정보
   */
  @Bean
  public Configuration ehcacheConfiguration() {
    Configuration cfg = new Configuration();

    // NOTE: EhCache 는 미리 cache 를 정의해주어야 한다. ( resources 의 ehcache.xml 을 보면 자세한 속성 및 설명이 있습니다 )
    // 다른 캐시 서버는 그럴 필요가 없다. (Memcached, Redis, Hazelcast, Apache Ignite)
    //
    cfg.addCache(new CacheConfiguration("ehcache-actors", 10000));
    cfg.addCache(new CacheConfiguration("ehcache-company", 10000));
    cfg.setName("mybatis-ehcache-manager-by-java-config");
    return cfg;
  }

  /**
   * EhCache 의 CacheManager
   *
   * @param cfg EhCache 의 환경설정
   */
  @Bean
  public net.sf.ehcache.CacheManager cacheManager(Configuration cfg) {
    return new net.sf.ehcache.CacheManager(cfg);
  }

  /**
   * Spring 의 CacheManager
   *
   * @param ehcacheManager EhCache의 CacheManager
   */
  @Bean
  public CacheManager ehcacheCacheManager(net.sf.ehcache.CacheManager ehcacheManager) {
    EhCacheCacheManager cm = new EhCacheCacheManager(ehcacheManager);
    if (cm.getCache("ehcache-actors") == null) {
      throw new RuntimeException("Cache 설정이 잘못되었습니다.");
    }
    log.info("Create EhCache CacheManagner instance.");
    return cm;
  }
}
