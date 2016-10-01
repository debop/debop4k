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

package debop4k.data.orm.springdata.examples.caching;

import debop4k.data.orm.jpa.config.databases.JpaTestConfiguration;
import debop4k.redisson.spring.cache.RedissonCacheKeyGenerator;
import debop4k.redisson.spring.cache.RedissonCacheManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableCaching
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
public class CachingConfiguration extends JpaTestConfiguration {

  @Override
  public String[] getMappedPackageNames() {
    return new String[]{User.class.getPackage().getName()};
  }

  @Bean
  public RedissonClient redissonClient() {

    org.redisson.config.Config config = new org.redisson.config.Config();
    config.useSingleServer().setAddress("localhost:6379");

    return Redisson.create(config);
  }

  @Bean
  public RedissonCacheManager redissonCacheManager(RedissonClient redissonClient) {
    RedissonCacheManager cm = new RedissonCacheManager(redissonClient);
    cm.setDefaultExpiryInMillis(60 * 1000);  // 60 sec
    return cm;
  }

  @Bean
  public KeyGenerator cacheKeyGenerator() {
    return new RedissonCacheKeyGenerator();
  }
}
