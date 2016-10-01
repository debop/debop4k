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

package debop4k.redisson.spring.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SnappyCodec;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
public class RedissonCacheConfiguration {

  @Bean
  public RedissonClient redissonClient() {

    Config config = new Config();
    config.useSingleServer().setAddress("localhost:6379");
    config.setCodec(new SnappyCodec());

    return Redisson.create(config);
  }

  @Bean
  public RedissonCacheManager redissonCacheManager(RedissonClient redissonClient) {
    RedissonCacheManager cm = new RedissonCacheManager(redissonClient);
    cm.setDefaultExpiryInMillis(60 * 1000);  // 60 sec
    return cm;
  }
}
