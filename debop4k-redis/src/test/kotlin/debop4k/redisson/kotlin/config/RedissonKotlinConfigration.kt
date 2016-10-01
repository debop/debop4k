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

package debop4k.redisson.kotlin.config

import debop4k.redisson.DEFAULT_ADDRESS
import debop4k.redisson.DEFAULT_HOST
import debop4k.redisson.DEFAULT_PORT
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.client.RedisClient
import org.redisson.codec.SnappyCodec
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RedissonKotlinConfigration {

  @Bean
  open fun redissonConfig(): Config {
    val config = Config()

    config.useSingleServer().setAddress(DEFAULT_ADDRESS)
    //        .setRetryAttempts(3)
    //        .setRetryInterval(1000);

    if (config.codec == null) {
      config.codec = SnappyCodec()
    }

    return config
  }

  @Bean
  open fun redisClient(): RedisClient {
    return RedisClient(DEFAULT_HOST, DEFAULT_PORT)
  }

  @Bean
  open fun redissonClient(config: Config): RedissonClient {
    return Redisson.create(config)
  }

  @Bean
  open fun redissonReactiveClient(config: Config): RedissonReactiveClient {
    return Redisson.createReactive(config)
  }
}