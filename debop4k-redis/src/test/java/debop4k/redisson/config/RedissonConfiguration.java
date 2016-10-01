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

package debop4k.redisson.config;

import debop4k.redisson.Redissonx;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.RedisClient;
import org.redisson.codec.SnappyCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

  @Bean
  public Config redissonConfig() {
    Config config = new Config();

    config.useSingleServer()
          .setAddress(Redissonx.DEFAULT_ADDRESS);
//        .setRetryAttempts(3)
//        .setRetryInterval(1000);

    if (config.getCodec() == null) {
      config.setCodec(new SnappyCodec());
    }

    return config;
  }

  @Bean
  public RedisClient redisClient() {
    return new RedisClient(Redissonx.DEFAULT_HOST, Redissonx.DEFAULT_PORT);
  }

  @Bean
  public RedissonClient redissonClient(Config config) {
    return Redisson.create(config);
  }

  @Bean
  public RedissonReactiveClient redissonReactiveClient(Config config) {
    return Redisson.createReactive(config);
  }
}
