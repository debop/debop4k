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

package debop4k.redisson.spring.boot.autoconfigure

import debop4k.core.uninitialized
import debop4k.redisson.configFromYaml
import debop4k.redisson.configWithSingleServer
import debop4k.redisson.spring.factory.RedissonReactiveTemplateFactoryBean
import debop4k.redisson.spring.factory.RedissonTemplateFactoryBean
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.inject.Inject

/**
 * RedissonAutoConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableConfigurationProperties(RedissonProperties::class, RedissonProperties::class)
open class RedissonAutoConfiguration {

  private val log = LoggerFactory.getLogger(javaClass)

  @Inject val redissonProps: RedissonProperties = uninitialized()
  @Inject val redissonConfig: Config = uninitialized()

  @Bean(name = arrayOf("redissonConfig"))
  open fun redissonConfig(): Config {
    try {
      if (redissonProps.config != null)
        return configFromYaml(redissonProps.config!!.inputStream)
      else
        return configWithSingleServer()
    } catch(e: Exception) {
      return configWithSingleServer()
    }
  }

  @Bean
  open fun redissonClient(cfg: Config): RedissonClient {
    return Redisson.create(cfg)
  }

  @Bean
  open fun redissonReactiveClient(cfg: Config): RedissonReactiveClient {
    return Redisson.createReactive(cfg)
  }

  @Bean
  open fun redissonTemplateFactoryBean(cfg: Config): RedissonTemplateFactoryBean {
    return RedissonTemplateFactoryBean(cfg)
  }

  @Bean
  open fun redissonReactiveTemplateFactoryBean(cfg: Config): RedissonReactiveTemplateFactoryBean {
    return RedissonReactiveTemplateFactoryBean(cfg)
  }
}