/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.ignite.config

import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * IgniteSpringConfiguration
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
open class IgniteSpringConfiguration {

  @Bean
  open fun igniteConfiguration(): IgniteConfiguration {
    val cfg = IgniteConfiguration()
    cfg.asyncCallbackPoolSize = 16
    return cfg
  }

  @Bean
  open fun ignite(cfg: IgniteConfiguration): Ignite {
    return Ignition.start(cfg)
  }
}