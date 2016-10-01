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

package debop4k.redisson.spring.factory

import debop4k.redisson.spring.RedissonReactiveTemplate
import org.redisson.Redisson
import org.redisson.config.Config
import org.springframework.beans.factory.FactoryBean

/**
 * RedissonReactiveTemplateFactoryBean
 * @author sunghyouk.bae@gmail.com
 */
class RedissonReactiveTemplateFactoryBean(val config: Config) : FactoryBean<RedissonReactiveTemplate> {

  override fun getObject(): RedissonReactiveTemplate {
    val client = Redisson.createReactive(config)
    return RedissonReactiveTemplate(client)
  }

  override fun getObjectType(): Class<*> = RedissonReactiveTemplate::class.java

  override fun isSingleton(): Boolean = true


}